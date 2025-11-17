package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AuthSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_selection);

        Button btnGoToLogin = findViewById(R.id.btnGoToLogin);
        Button btnGoToCadastro = findViewById(R.id.btnGoToCadastro);

        btnGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(AuthSelectionActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnGoToCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(AuthSelectionActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
    }
}
