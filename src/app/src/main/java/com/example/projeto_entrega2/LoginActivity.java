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
import com.example.projeto_entrega2.model.User;

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
            getSupportActionBar().setDisplayShowTitleEnabled(false);
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

        new LoginAsyncTask(db).execute(email, password);
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, User> {
        private AppDatabase db;
        private String passwordInput;

        LoginAsyncTask(AppDatabase db) {
            this.db = db;
        }

        @Override
        protected User doInBackground(String... params) {
            passwordInput = params[1];
            return db.userDAO().findByEmail(params[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null && user.getPassword().equals(passwordInput)) {
                Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

                if ("CANTINA".equals(user.getProfileType())) {
                    Intent intent = new Intent(LoginActivity.this, CantinaPainelActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginActivity.this, VitrineActivity.class);
                    startActivity(intent);
                }
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Email ou senha inválidos.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
