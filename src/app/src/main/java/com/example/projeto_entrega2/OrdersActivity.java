package com.example.projeto_entrega2;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.adapter.MyOrdersAdapter;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Order;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyOrdersAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        db = AppDatabase.getDatabase(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Meus Pedidos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_view_my_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyOrdersAdapter();
        recyclerView.setAdapter(adapter);

        loadUserOrders();
    }

    private void loadUserOrders() {
        // ATENÇÃO: O userId está fixo como 1L. O ideal é obter o ID do usuário logado.
        long userId = 1L;
        new LoadUserOrdersAsyncTask().execute(userId);
    }

    private class LoadUserOrdersAsyncTask extends AsyncTask<Long, Void, List<Order>> {
        @Override
        protected List<Order> doInBackground(Long... userIds) {
            if (userIds.length == 0) return null;
            return db.orderDAO().getOrdersByUserId(userIds[0]);
        }

        @Override
        protected void onPostExecute(List<Order> orders) {
            if (orders != null) {
                adapter.setOrders(orders);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
