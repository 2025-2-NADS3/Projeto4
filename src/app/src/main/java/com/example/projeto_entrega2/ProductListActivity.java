package com.example.projeto_entrega2;

import android.content.Intent;
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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.adapter.ProdutoAdapter;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Produto;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";

    private RecyclerView recyclerView;
    private ProdutoAdapter produtoAdapter;
    private ProgressBar progressBar;
    private AppDatabase db;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        category = getIntent().getStringExtra(EXTRA_CATEGORY);
        if (category == null || category.isEmpty()) {
            Toast.makeText(this, "Categoria não especificada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(category);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        db = AppDatabase.getDatabase(getApplicationContext());

        setupRecyclerView();
        loadProductsFromDb();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            Intent intent = new Intent(this, CarrinhoActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        produtoAdapter = new ProdutoAdapter();
        recyclerView.setAdapter(produtoAdapter);

        produtoAdapter.setOnItemClickListener(produto -> {
            Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT, produto);
            startActivity(intent);
        });
    }

    private void loadProductsFromDb() {
        progressBar.setVisibility(View.VISIBLE);
        new LoadProductsAsyncTask().execute(category);
    }

    private class LoadProductsAsyncTask extends AsyncTask<String, Void, List<Produto>> {
        @Override
        protected List<Produto> doInBackground(String... params) {
            String categoryToLoad = params[0];
            return db.produtoDAO().getProductsByCategory(categoryToLoad);
        }

        @Override
        protected void onPostExecute(List<Produto> produtos) {
            super.onPostExecute(produtos);
            progressBar.setVisibility(View.GONE);
            if (produtos != null) {
                produtoAdapter.setProdutos(produtos);
            }
        }
    }
}
