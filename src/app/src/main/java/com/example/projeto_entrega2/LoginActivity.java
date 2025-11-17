package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnLogin;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Login");
        }

        db = AppDatabase.getDatabase(getApplicationContext());

        editTextEmail = findViewById(R.id.editTextLoginEmail);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> loginUsuario());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loginUsuario() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor, preencha email e senha", Toast.LENGTH_SHORT).show();
            return;
        }

        // Executa a tarefa de login com os dados corretos
        new LoginAsyncTask().execute(email, password);
    }

    // AsyncTask corrigida para usar Usuario e usuarioDAO
    private class LoginAsyncTask extends AsyncTask<String, Void, Usuario> {

        @Override
        protected Usuario doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            // CORREÇÃO: Usando usuarioDAO e findByEmailAndPassword
            return db.usuarioDAO().findByEmailAndPassword(email, password);
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            if (usuario != null) {
                Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

                // CORREÇÃO: Usando isCantina() para verificar o perfil
                if (usuario.isCantina()) {
                    Intent intent = new Intent(LoginActivity.this, CantinaPainelActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginActivity.this, VitrineActivity.class);
                    startActivity(intent);
                }
                finish(); // Fecha a tela de login após o sucesso
            } else {
                Toast.makeText(LoginActivity.this, "Email ou senha inválidos.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
