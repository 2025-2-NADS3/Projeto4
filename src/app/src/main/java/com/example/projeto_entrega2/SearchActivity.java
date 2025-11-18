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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.adapter.ProdutoAdapter;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Produto;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchRecyclerView;
    private ProdutoAdapter produtoAdapter;
    private List<Produto> productList = new ArrayList<>();
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = AppDatabase.getDatabase(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Pesquisar Produtos");
        }

        setupRecyclerView();
        setupBottomNavigationView();
    }

    private void setupRecyclerView() {
        searchRecyclerView = findViewById(R.id.recycler_view_search);
        searchRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        produtoAdapter = new ProdutoAdapter(this, productList);
        searchRecyclerView.setAdapter(produtoAdapter);
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_search) {
                return true; // Não faz nada, já estamos aqui
            }

            Intent intent = null;
            if (itemId == R.id.nav_home) {
                intent = new Intent(this, VitrineActivity.class);
            } else if (itemId == R.id.nav_orders) {
                intent = new Intent(this, OrdersActivity.class);
            } else if (itemId == R.id.nav_coupons) {
                intent = new Intent(this, CouponsActivity.class);
            } else if (itemId == R.id.nav_profile) {
                intent = new Intent(this, ProfileActivity.class);
            }

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
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
            if (params.length == 0) return new ArrayList<>();
            // CORREÇÃO: Usando o nome correto do método do DAO
            return db.produtoDAO().searchProducts(params[0]);
        }

        @Override
        protected void onPostExecute(List<Produto> produtos) {
            productList.clear();
            if (produtos != null) {
                productList.addAll(produtos);
            }
            produtoAdapter.notifyDataSetChanged();
        }
    }
}
