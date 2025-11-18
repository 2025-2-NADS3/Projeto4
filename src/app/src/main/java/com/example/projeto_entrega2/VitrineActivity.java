package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VitrineActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitrine);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Comedoria da Tia");
        }

        // --- Listeners para os cards ---
        CardView searchBar = findViewById(R.id.search_bar_card);
        LinearLayout cardSalgados = findViewById(R.id.cardSalgados);
        LinearLayout cardDoces = findViewById(R.id.cardDoces);
        LinearLayout cardBebidas = findViewById(R.id.cardBebidas);
        LinearLayout cardRefeicoes = findViewById(R.id.cardRefeicoes);

        searchBar.setOnClickListener(this);
        cardSalgados.setOnClickListener(this);
        cardDoces.setOnClickListener(this);
        cardBebidas.setOnClickListener(this);
        cardRefeicoes.setOnClickListener(this);

        // --- Lógica do Menu de Navegação Inferior (CORRIGIDA) ---
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true; // Não faz nada, já estamos aqui
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_coupons) {
                startActivity(new Intent(getApplicationContext(), CouponsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_orders) {
                startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
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
            startActivity(new Intent(this, CarrinhoActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.search_bar_card) {
            startActivity(new Intent(this, SearchActivity.class));
            return;
        }

        String category = "";
        if (id == R.id.cardSalgados) {
            category = "Salgados";
        } else if (id == R.id.cardDoces) {
            category = "Doces";
        } else if (id == R.id.cardBebidas) {
            category = "Bebidas";
        } else if (id == R.id.cardRefeicoes) {
            category = "Refeições";
        }

        if (!category.isEmpty()) {
            Intent intent = new Intent(VitrineActivity.this, ProductListActivity.class);
            intent.putExtra(ProductListActivity.EXTRA_CATEGORY, category);
            startActivity(intent);
        }
    }
}
