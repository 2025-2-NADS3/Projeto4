package com.example.projeto_entrega2;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.adapter.OrderManagementAdapter;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Order;

import java.util.List;

public class ManageOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderManagementAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        db = AppDatabase.getDatabase(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gerenciar Pedidos");
        }

        recyclerView = findViewById(R.id.recycler_view_manage_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OrderManagementAdapter(this);
        recyclerView.setAdapter(adapter);

        loadOrders();
    }

    private void loadOrders() {
        new LoadOrdersAsyncTask().execute();
    }

    private class LoadOrdersAsyncTask extends AsyncTask<Void, Void, List<Order>> {
        @Override
        protected List<Order> doInBackground(Void... voids) {
            // Por padrão, carrega todos os pedidos. Pode ser filtrado depois.
            return db.orderDAO().getAllOrders();
        }

        @Override
        protected void onPostExecute(List<Order> orders) {
            adapter.setOrders(orders);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
