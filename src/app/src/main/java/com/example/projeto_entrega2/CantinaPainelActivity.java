package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.adapter.ProdutoAdapter;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Produto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CantinaPainelActivity extends AppCompatActivity {

    private static final int ADD_PRODUCT_REQUEST = 1;
    private static final int EDIT_PRODUCT_REQUEST = 2;

    private RecyclerView recyclerView;
    private ProdutoAdapter produtoAdapter;
    private ProgressBar progressBar;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cantina_painel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Painel de Controle");
        }

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        db = AppDatabase.getDatabase(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.fab_add_product);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(CantinaPainelActivity.this, AddEditProductActivity.class);
            startActivityForResult(intent, ADD_PRODUCT_REQUEST);
        });

        // CORREÇÃO: Ligando o novo botão para gerenciar pedidos
        Button manageOrdersButton = findViewById(R.id.button_manage_orders);
        manageOrdersButton.setOnClickListener(v -> {
            Intent intent = new Intent(CantinaPainelActivity.this, ManageOrdersActivity.class);
            startActivity(intent);
        });

        setupRecyclerView();
        setupItemTouchHelper();
        buscarDadosDoBanco();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == ADD_PRODUCT_REQUEST || requestCode == EDIT_PRODUCT_REQUEST) && resultCode == RESULT_OK) {
            buscarDadosDoBanco(); // Recarrega a lista após adicionar ou editar
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        produtoAdapter = new ProdutoAdapter();
        recyclerView.setAdapter(produtoAdapter);
        
        // Lógica de clique para EDITAR
        produtoAdapter.setOnItemClickListener(produto -> {
            Intent intent = new Intent(CantinaPainelActivity.this, AddEditProductActivity.class);
            intent.putExtra(AddEditProductActivity.EXTRA_PRODUCT, produto);
            startActivityForResult(intent, EDIT_PRODUCT_REQUEST);
        });
    }

    private void setupItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Produto produtoParaDeletar = produtoAdapter.getProductAt(position);
                new DeleteProductAsyncTask().execute(produtoParaDeletar);
            }
        }).attachToRecyclerView(recyclerView);
    }

    private class DeleteProductAsyncTask extends AsyncTask<Produto, Void, Void> {
        @Override
        protected Void doInBackground(Produto... produtos) {
            db.produtoDAO().delete(produtos[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(CantinaPainelActivity.this, "Produto excluído!", Toast.LENGTH_SHORT).show();
            buscarDadosDoBanco(); // Recarrega a lista para remover o item visualmente
        }
    }

    // ... (código de menu e busca no banco permanecem iguais)

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dados_salvos_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            deleteAllData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.produtoDAO().deleteAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                produtoAdapter.setProdutos(new ArrayList<>());
                Toast.makeText(CantinaPainelActivity.this, "Todos os dados foram apagados!", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void buscarDadosDoBanco() {
        progressBar.setVisibility(View.VISIBLE);
        new SelectFromDbAsyncTask(this).execute();
    }

    private static class SelectFromDbAsyncTask extends AsyncTask<Void, Void, List<Produto>> {
        private final CantinaPainelActivity activity;

        SelectFromDbAsyncTask(CantinaPainelActivity activity) {
            this.activity = activity;
        }

        @Override
        protected List<Produto> doInBackground(Void... voids) {
            return activity.db.produtoDAO().getAll();
        }

        @Override
        protected void onPostExecute(List<Produto> produtos) {
            if (activity != null) {
                activity.progressBar.setVisibility(View.GONE);
                activity.produtoAdapter.setProdutos(produtos);
            }
        }
    }
}
