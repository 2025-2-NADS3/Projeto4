package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.adapter.ProdutoAdapter;
import com.example.projeto_entrega2.model.Produto;
import com.example.projeto_entrega2.repository.ProdutoRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProdutoAdapter produtoAdapter;
    private ProdutoRepository produtoRepository;
    private ProgressBar progressBar;
    private Button btnVerDadosSalvos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        btnVerDadosSalvos = findViewById(R.id.btnVerDadosSalvos);

        produtoRepository = new ProdutoRepository(getApplicationContext());

        setupRecyclerView();
        carregarCardapio();

        btnVerDadosSalvos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DadosSalvosActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // CORREÇÃO: O construtor do adapter não recebe argumentos
        produtoAdapter = new ProdutoAdapter();
        recyclerView.setAdapter(produtoAdapter);

        produtoAdapter.setOnItemClickListener(produto -> {
            Toast.makeText(MainActivity.this, "Clicou em: " + produto.getNome(), Toast.LENGTH_SHORT).show();
        });
    }

    private void carregarCardapio() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        produtoRepository.buscarProdutos(new ProdutoRepository.RepositoryCallback<List<Produto>>() {
            @Override
            public void onComplete(List<Produto> result) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (result != null && !result.isEmpty()) {
                    // CORREÇÃO: O método para atualizar os dados se chama 'setProdutos'
                    produtoAdapter.setProdutos(result);
                } else {
                    Toast.makeText(MainActivity.this, "Não foi possível carregar o cardápio.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
