package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button btnApiProducts = findViewById(R.id.btnApiProducts);
        Button btnSavedProducts = findViewById(R.id.btnSavedProducts);
        Button btnAnalytics = findViewById(R.id.btnAnalytics);

        btnApiProducts.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnSavedProducts.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, DadosSalvosActivity.class);
            startActivity(intent);
        });

        btnAnalytics.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, AnalyticsActivity.class);
            startActivity(intent);
        });
    }
}
