package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.adapter.ProdutoAdapter;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Produto;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ProdutoAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = AppDatabase.getDatabase(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProdutoAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(produto -> {
            Intent intent = new Intent(SearchActivity.this, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT, produto);
            startActivity(intent);
        });

        // --- Lógica do Menu de Navegação Inferior ---
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), VitrineActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_search) {
                return true;
            } else if (itemId == R.id.nav_coupons) {
                startActivity(new Intent(getApplicationContext(), CouponsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_orders) {
                startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }

    // ... (outros métodos permanecem iguais)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchItem.expandActionView();
        searchView.setIconified(false);
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });

        return true;
    }

    private void performSearch(String query) {
        String searchQuery = "%" + query + "%";
        new SearchAsyncTask().execute(searchQuery);
    }

    private class SearchAsyncTask extends AsyncTask<String, Void, List<Produto>> {
        @Override
        protected List<Produto> doInBackground(String... params) {
            return db.produtoDAO().searchProducts(params[0]);
        }

        @Override
        protected void onPostExecute(List<Produto> produtos) {
            adapter.setProdutos(produtos);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); 
        return true;
    }
}
