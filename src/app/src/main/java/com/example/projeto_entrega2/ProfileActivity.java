package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Meu Perfil");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        // CORREÇÃO: Lógica de navegação com flags para limpar a pilha de telas
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                return true; // Não faz nada, já estamos aqui
            }

            Intent intent = null;
            if (itemId == R.id.nav_home) {
                intent = new Intent(this, VitrineActivity.class);
            } else if (itemId == R.id.nav_search) {
                intent = new Intent(this, SearchActivity.class);
            } else if (itemId == R.id.nav_orders) {
                intent = new Intent(this, OrdersActivity.class);
            } else if (itemId == R.id.nav_coupons) {
                intent = new Intent(this, CouponsActivity.class);
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
}
