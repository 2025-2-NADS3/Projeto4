package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.CartItem;
import com.example.projeto_entrega2.model.Order;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CheckoutActivity extends AppCompatActivity {

    public static final String EXTRA_TOTAL_AMOUNT = "EXTRA_TOTAL_AMOUNT";
    private AppDatabase db;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = AppDatabase.getDatabase(getApplicationContext());
        totalAmount = getIntent().getDoubleExtra(EXTRA_TOTAL_AMOUNT, 0.0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pagamento");
        }

        TextView totalTextView = findViewById(R.id.text_view_total_checkout);
        Locale ptBr = new Locale("pt", "BR");
        totalTextView.setText("Total: " + NumberFormat.getCurrencyInstance(ptBr).format(totalAmount));

        RadioGroup paymentRadioGroup = findViewById(R.id.radio_group_payment_checkout);
        Button confirmOrderButton = findViewById(R.id.button_confirm_order);

        confirmOrderButton.setOnClickListener(v -> {
            if (paymentRadioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Por favor, selecione um método de pagamento", Toast.LENGTH_SHORT).show();
                return;
            }
            new FinalizeOrderAsyncTask().execute();
        });
    }

    private String generatePickupCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private class FinalizeOrderAsyncTask extends AsyncTask<Void, Void, Long> {

        private List<CartItem> cartItems;

        @Override
        protected Long doInBackground(Void... voids) {
            cartItems = db.cartItemDAO().getAll();
            if (cartItems == null || cartItems.isEmpty()) {
                return -1L; // Retorna -1 se o carrinho estiver vazio
            }

            Gson gson = new Gson();
            String itemsJson = gson.toJson(cartItems);

            String pickupCode = generatePickupCode();
            long userId = 1L; // ATENÇÃO: O userId está fixo
            String status = "Pendente";
            long timestamp = System.currentTimeMillis();
            
            Order newOrder = new Order(userId, itemsJson, totalAmount, status, timestamp, pickupCode);

            long newOrderId = db.orderDAO().insert(newOrder);
            db.cartItemDAO().deleteAll();

            return newOrderId;
        }

        @Override
        protected void onPostExecute(Long newOrderId) {
            super.onPostExecute(newOrderId);
            if (newOrderId != -1L) {
                Toast.makeText(CheckoutActivity.this, "Pedido confirmado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CheckoutActivity.this, OrderStatusActivity.class);
                intent.putExtra(OrderStatusActivity.EXTRA_ORDER_ID, newOrderId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(CheckoutActivity.this, "Ocorreu um erro ao finalizar o pedido.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
