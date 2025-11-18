package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button continueButton = findViewById(R.id.button_continue);
        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, AuthSelectionActivity.class);
            startActivity(intent);
            finish(); // Finaliza a WelcomeActivity para que o usuário não volte para ela
        });
    }
}
