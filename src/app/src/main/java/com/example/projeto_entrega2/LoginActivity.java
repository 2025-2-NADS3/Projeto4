package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = AppDatabase.getDatabase(getApplicationContext());

        editTextEmail = findViewById(R.id.editTextLoginEmail);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        ImageButton backButton = findViewById(R.id.back_button_login);

        btnLogin.setOnClickListener(v -> loginUser());

        // Ação para o botão de voltar
        backButton.setOnClickListener(v -> {
            onBackPressed(); // Volta para a tela anterior (AuthSelectionActivity)
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        new LoginAsyncTask().execute(email, password);
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, Usuario> {
        @Override
        protected Usuario doInBackground(String... credentials) {
            return db.usuarioDAO().login(credentials[0], credentials[1]);
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            if (usuario != null) {
                Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                if (usuario.isCantina()) {
                    Intent intent = new Intent(LoginActivity.this, CantinaPainelActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginActivity.this, VitrineActivity.class);
                    startActivity(intent);
                }
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
