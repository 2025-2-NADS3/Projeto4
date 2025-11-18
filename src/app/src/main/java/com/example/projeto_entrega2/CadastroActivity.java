package com.example.projeto_entrega2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextNomeCompleto, editTextNomeUsuario, editTextEmail, editTextSenha, editTextConfirmarSenha;
    private SwitchCompat switchIsCantina;
    private Button buttonCriarConta;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        db = AppDatabase.getDatabase(getApplicationContext());

        editTextNomeCompleto = findViewById(R.id.edit_text_nome_completo);
        editTextNomeUsuario = findViewById(R.id.edit_text_nome_usuario);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextSenha = findViewById(R.id.edit_text_senha);
        editTextConfirmarSenha = findViewById(R.id.edit_text_confirmar_senha);
        switchIsCantina = findViewById(R.id.switch_is_cantina);
        buttonCriarConta = findViewById(R.id.button_criar_conta);

        buttonCriarConta.setOnClickListener(v -> criarNovaConta());
    }

    private void criarNovaConta() {
        String nomeCompleto = editTextNomeCompleto.getText().toString().trim();
        String nomeUsuario = editTextNomeUsuario.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();
        String confirmarSenha = editTextConfirmarSenha.getText().toString().trim();
        boolean isCantina = switchIsCantina.isChecked();

        if (TextUtils.isEmpty(nomeCompleto) || TextUtils.isEmpty(nomeUsuario) || TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        // CORREÇÃO DEFINITIVA: Usando o construtor correto para criar o objeto Usuario
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
            finish();
        }
    }
}
