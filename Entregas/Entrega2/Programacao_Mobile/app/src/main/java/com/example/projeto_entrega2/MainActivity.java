package com.example.projeto_entrega2;

import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewProdutos);
        progressBar = findViewById(R.id.progressBar);

        produtoRepository = new ProdutoRepository(getApplicationContext());

        setupRecyclerView();
        carregarCardapio();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                    produtoAdapter.setProdutos(result);
                } else {
                    Toast.makeText(MainActivity.this, "Não foi possível carregar o cardápio.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
