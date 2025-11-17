package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class VitrineActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitrine);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Cardápio - Categorias");
        }

        CardView cardSalgados = findViewById(R.id.cardSalgados);
        CardView cardDoces = findViewById(R.id.cardDoces);
        CardView cardBebidas = findViewById(R.id.cardBebidas);
        CardView cardRefeicoes = findViewById(R.id.cardRefeicoes);

        cardSalgados.setOnClickListener(this);
        cardDoces.setOnClickListener(this);
        cardBebidas.setOnClickListener(this);
        cardRefeicoes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String category = "";
        int id = v.getId();
        if (id == R.id.cardSalgados) {
            category = "Salgados";
        } else if (id == R.id.cardDoces) {
            category = "Doces";
        } else if (id == R.id.cardBebidas) {
            category = "Bebidas";
        } else if (id == R.id.cardRefeicoes) {
            category = "Refeições";
        }

        Intent intent = new Intent(VitrineActivity.this, ProductListActivity.class);
        intent.putExtra(ProductListActivity.EXTRA_CATEGORY, category);
        startActivity(intent);
    }
}
