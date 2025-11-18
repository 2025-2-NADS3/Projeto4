package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class AuthSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_selection);

        // Encontrando os novos botões pelo ID
        ImageButton backButton = findViewById(R.id.back_button);
        Button loginButton = findViewById(R.id.button_login);
        Button registerButton = findViewById(R.id.button_register);

        // Ação para o botão de voltar
        backButton.setOnClickListener(v -> {
            onBackPressed(); // Volta para a tela anterior (WelcomeActivity)
        });

        // Ação para o botão de Login
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(AuthSelectionActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Ação para o botão de Cadastro
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(AuthSelectionActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
    }
}
