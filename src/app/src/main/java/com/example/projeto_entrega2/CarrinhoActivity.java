package com.example.projeto_entrega2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.adapter.CartAdapter;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CarrinhoActivity extends AppCompatActivity implements CartAdapter.OnCartItemInteractionListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView textViewTotal;
    private AppDatabase db;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Carrinho de Compras");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getDatabase(getApplicationContext());
        recyclerView = findViewById(R.id.recycler_view_carrinho);
        textViewTotal = findViewById(R.id.text_view_total);
        Button buttonFinalizar = findViewById(R.id.button_finalizar_compra);

        setupRecyclerView();
        loadCartItems();

        buttonFinalizar.setOnClickListener(v -> finalizePurchase());
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this);
        recyclerView.setAdapter(cartAdapter);
    }

    private void loadCartItems() {
        new LoadCartItemsAsyncTask().execute();
    }

    private void calculateTotal() {
        double total = 0;
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                total += item.getProductPrice() * item.getQuantity();
            }
        }
        Locale ptBr = new Locale("pt", "BR");
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(ptBr);
        textViewTotal.setText("Total: " + formatoMoeda.format(total));
    }

    private void finalizePurchase() {
        new ClearCartAsyncTask().execute();
    }

    @Override
    public void onIncreaseQuantity(CartItem item) {
        item.setQuantity(item.getQuantity() + 1);
        new UpdateCartItemAsyncTask().execute(item);
    }

    @Override
    public void onDecreaseQuantity(CartItem item) {
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            new UpdateCartItemAsyncTask().execute(item);
        } else {
            // Se a quantidade for 1, diminuir significa remover
            onRemoveItem(item);
        }
    }

    @Override
    public void onRemoveItem(CartItem item) {
        new RemoveCartItemAsyncTask().execute(item);
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // --- AsyncTasks para operações de banco de dados ---

    private class LoadCartItemsAsyncTask extends AsyncTask<Void, Void, List<CartItem>> {
        @Override
        protected List<CartItem> doInBackground(Void... voids) {
            return db.cartItemDAO().getAllItems();
        }

        @Override
        protected void onPostExecute(List<CartItem> items) {
            cartItems = items;
            cartAdapter.setCartItems(cartItems);
            calculateTotal();
        }
    }

    private class UpdateCartItemAsyncTask extends AsyncTask<CartItem, Void, Void> {
        @Override
        protected Void doInBackground(CartItem... items) {
            db.cartItemDAO().update(items[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadCartItems(); // Recarrega para atualizar a UI
        }
    }

    private class RemoveCartItemAsyncTask extends AsyncTask<CartItem, Void, Void> {
        @Override
        protected Void doInBackground(CartItem... items) {
            db.cartItemDAO().delete(items[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadCartItems(); // Recarrega para atualizar a UI
        }
    }

    private class ClearCartAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            db.cartItemDAO().deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(CarrinhoActivity.this, "Pagamento efetuado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
