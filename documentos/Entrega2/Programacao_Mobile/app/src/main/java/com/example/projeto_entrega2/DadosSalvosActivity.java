package com.example.projeto_entrega2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.adapter.ProdutoAdapter;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class DadosSalvosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProdutoAdapter produtoAdapter;
    private ProgressBar progressBar;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_salvos);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        db = AppDatabase.getDatabase(getApplicationContext());

        setupRecyclerView();
        buscarDadosDoBanco();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        produtoAdapter = new ProdutoAdapter();
        recyclerView.setAdapter(produtoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dados_salvos_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            deleteAllData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.produtoDAO().deleteAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                produtoAdapter.setProdutos(new ArrayList<>()); // Limpa a lista na UI
                Toast.makeText(DadosSalvosActivity.this, "Dados apagados!", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void buscarDadosDoBanco() {
        progressBar.setVisibility(View.VISIBLE);
        new SelectFromDbAsyncTask(this).execute();
    }

    private static class SelectFromDbAsyncTask extends AsyncTask<Void, Void, List<Produto>> {
        private final DadosSalvosActivity activity;

        SelectFromDbAsyncTask(DadosSalvosActivity activity) {
            this.activity = activity;
        }

        @Override
        protected List<Produto> doInBackground(Void... voids) {
            return activity.db.produtoDAO().getAll();
        }

        @Override
        protected void onPostExecute(List<Produto> produtos) {
            if (activity != null) {
                activity.progressBar.setVisibility(View.GONE);
                activity.produtoAdapter.setProdutos(produtos);
            }
        }
    }
}
