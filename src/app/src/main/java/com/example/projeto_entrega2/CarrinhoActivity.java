package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.adapter.CarrinhoAdapter;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.CartItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CarrinhoActivity extends AppCompatActivity {

    private CarrinhoAdapter adapter;
    private TextView totalTextView;
    private AppDatabase db;
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        db = AppDatabase.getDatabase(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Carrinho");
        }

        totalTextView = findViewById(R.id.text_view_total);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_carrinho);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new CarrinhoAdapter(this, this::updateTotal);
        recyclerView.setAdapter(adapter);

        Button goToPaymentButton = findViewById(R.id.button_go_to_payment);
        goToPaymentButton.setOnClickListener(v -> goToPayment());

        loadCartItems();
    }

    private void loadCartItems() {
        new LoadCartItemsAsyncTask().execute();
    }

    private void updateTotal(List<CartItem> items) {
        this.cartItems = items;
        double total = 0;
        for (CartItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        Locale ptBr = new Locale("pt", "BR");
        totalTextView.setText("Total: " + NumberFormat.getCurrencyInstance(ptBr).format(total));
    }

    private void goToPayment() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Seu carrinho está vazio", Toast.LENGTH_SHORT).show();
            return;
        }

        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }

        Intent intent = new Intent(CarrinhoActivity.this, CheckoutActivity.class);
        intent.putExtra(CheckoutActivity.EXTRA_TOTAL_AMOUNT, total);
        startActivity(intent);
    }

    private class LoadCartItemsAsyncTask extends AsyncTask<Void, Void, List<CartItem>> {
        @Override
        protected List<CartItem> doInBackground(Void... voids) {
            return db.cartItemDAO().getAll();
        }

        @Override
        protected void onPostExecute(List<CartItem> cartItems) {
            adapter.setItems(cartItems);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
