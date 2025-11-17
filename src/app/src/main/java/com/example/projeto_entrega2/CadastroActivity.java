package com.example.projeto_entrega2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText nomeCompletoEditText;
    private EditText nomeUsuarioEditText;
    private EditText emailEditText;
    private EditText senhaEditText;
    private EditText confirmarSenhaEditText;
    private SwitchCompat isCantinaSwitch; // NOVO SWITCH
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        db = AppDatabase.getDatabase(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cadastro");
        }

        nomeCompletoEditText = findViewById(R.id.edit_text_nome_completo);
        nomeUsuarioEditText = findViewById(R.id.edit_text_nome_usuario);
        emailEditText = findViewById(R.id.edit_text_email);
        senhaEditText = findViewById(R.id.edit_text_senha);
        confirmarSenhaEditText = findViewById(R.id.edit_text_confirmar_senha);
        isCantinaSwitch = findViewById(R.id.switch_is_cantina); // OBTENDO A REFERÊNCIA

        Button criarContaButton = findViewById(R.id.button_criar_conta);
        criarContaButton.setOnClickListener(v -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        String nomeCompleto = nomeCompletoEditText.getText().toString().trim();
        String nomeUsuario = nomeUsuarioEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();
        String confirmarSenha = confirmarSenhaEditText.getText().toString().trim();
        boolean isCantina = isCantinaSwitch.isChecked(); // LENDO O VALOR DO SWITCH

        if (TextUtils.isEmpty(nomeCompleto) || TextUtils.isEmpty(nomeUsuario) || TextUtils.isEmpty(email) || TextUtils.isEmpty(senha) || TextUtils.isEmpty(confirmarSenha)) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Adicionar verificação se usuário ou email já existem

        // CORREÇÃO: Usando o valor do Switch para definir o tipo de conta
        Usuario novoUsuario = new Usuario(nomeCompleto, nomeUsuario, email, senha, isCantina);
        new InsertUsuarioAsyncTask().execute(novoUsuario);
    }

    private class InsertUsuarioAsyncTask extends AsyncTask<Usuario, Void, Void> {
        @Override
        protected Void doInBackground(Usuario... usuarios) {
            db.usuarioDAO().insert(usuarios[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(CadastroActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a tela de cadastro
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
