package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Order;

public class OrderStatusActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID = "com.example.projeto_entrega2.EXTRA_ORDER_ID";

    private TextView pickupCodeTextView;
    private Button backToHomeButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        db = AppDatabase.getDatabase(getApplicationContext());
        pickupCodeTextView = findViewById(R.id.text_view_pickup_code);
        backToHomeButton = findViewById(R.id.button_back_to_home);

        long orderId = getIntent().getLongExtra(EXTRA_ORDER_ID, -1);

        if (orderId != -1) {
            loadOrderDetails(orderId);
        } else {
            Toast.makeText(this, "Erro: ID do pedido não encontrado", Toast.LENGTH_LONG).show();
            pickupCodeTextView.setText("N/A");
        }

        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderStatusActivity.this, VitrineActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadOrderDetails(long orderId) {
        new LoadOrderAsyncTask().execute(orderId);
    }

    private class LoadOrderAsyncTask extends AsyncTask<Long, Void, Order> {
        @Override
        protected Order doInBackground(Long... params) {
            if (params.length > 0) {
                return db.orderDAO().getOrderById(params[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Order order) {
            if (order != null) {
                pickupCodeTextView.setText(order.getPickupCode());
            } else {
                Toast.makeText(OrderStatusActivity.this, "Pedido não encontrado", Toast.LENGTH_SHORT).show();
                pickupCodeTextView.setText("Erro");
            }
        }
    }
}
