package com.example.projeto_entrega2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.adapter.ProdutoAdapter;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Produto;
import com.example.projeto_entrega2.model.ProdutoDAO;

import java.util.List;

public class DadosSalvosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProdutoAdapter produtoAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_salvos);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // CORREÇÃO 1: O construtor do adapter não recebe argumentos
        produtoAdapter = new ProdutoAdapter();
        recyclerView.setAdapter(produtoAdapter);

        // Busca os dados diretamente do banco, sem passar pela rede
        buscarDadosDoBanco();
    }

    private void buscarDadosDoBanco() {
        progressBar.setVisibility(View.VISIBLE);
        new SelectFromDbAsyncTask(this).execute();
    }

    // AsyncTask para buscar dados do banco em uma thread de fundo
    private static class SelectFromDbAsyncTask extends AsyncTask<Void, Void, List<Produto>> {

        private DadosSalvosActivity activity;

        SelectFromDbAsyncTask(DadosSalvosActivity activity) {
            this.activity = activity;
        }

        @Override
        protected List<Produto> doInBackground(Void... voids) {
            // Acessa o DAO e busca todos os produtos
            ProdutoDAO dao = AppDatabase.getDatabase(activity.getApplicationContext()).produtoDAO();
            return dao.getAll();
        }

        @Override
        protected void onPostExecute(List<Produto> produtos) {
            super.onPostExecute(produtos);
            if (activity != null) {
                activity.progressBar.setVisibility(View.GONE);
                // CORREÇÃO 2: O método para atualizar os dados se chama 'setProdutos'
                activity.produtoAdapter.setProdutos(produtos);
            }
        }
    }
}
