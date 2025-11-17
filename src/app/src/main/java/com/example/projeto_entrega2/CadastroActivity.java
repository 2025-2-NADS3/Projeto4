package com.example.projeto_entrega2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.User;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private RadioGroup radioGroupProfile;
    private Button btnCadastrar;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        db = AppDatabase.getDatabase(getApplicationContext());

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        radioGroupProfile = findViewById(R.id.radioGroupProfile);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(v -> cadastrarUsuario());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void cadastrarUsuario() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        int selectedProfileId = radioGroupProfile.getCheckedRadioButtonId();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || selectedProfileId == -1) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedProfileId);
        String profileType = selectedRadioButton.getText().toString().equalsIgnoreCase("ALUNO") ? "ALUNO" : "CANTINA";

        User newUser = new User(email, password, profileType);

        new InsertUserAsyncTask(db).execute(newUser);
    }

    private class InsertUserAsyncTask extends AsyncTask<User, Void, Boolean> {
        private AppDatabase db;

        InsertUserAsyncTask(AppDatabase db) {
            this.db = db;
        }

        @Override
        protected Boolean doInBackground(User... users) {
            try {
                db.userDAO().insert(users[0]);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(CadastroActivity.this, "Erro ao cadastrar. O email já pode estar em uso.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
