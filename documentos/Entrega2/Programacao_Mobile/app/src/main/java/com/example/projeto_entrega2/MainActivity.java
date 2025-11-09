package com.example.projeto_entrega2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projeto_entrega2.adapter.ProdutoAdapter;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Produto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProdutoAdapter produtoAdapter;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;
    private AppDatabase db;

    private static final String URL = "https://dummyjson.com/products/category/groceries";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        requestQueue = Volley.newRequestQueue(this);
        db = AppDatabase.getDatabase(getApplicationContext());

        setupRecyclerView();
        carregarCardapio();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        produtoAdapter = new ProdutoAdapter();
        recyclerView.setAdapter(produtoAdapter);

        produtoAdapter.setOnItemClickListener(produto -> {
            salvarProdutoNoBanco(produto);
        });
    }

    private void salvarProdutoNoBanco(Produto produto) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.produtoDAO().insert(produto);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(MainActivity.this, produto.getNome() + " salvo!", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void carregarCardapio() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL, null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    try {
                        String productsJson = response.getJSONArray("products").toString();
                        Type listType = new TypeToken<List<Produto>>() {}.getType();
                        List<Produto> produtos = new Gson().fromJson(productsJson, listType);
                        produtoAdapter.setProdutos(produtos);
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Erro ao parsear o JSON", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Falha na comunicação com a API: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}
