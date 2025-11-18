package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projeto_entrega2.adapter.OrderAdapter;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();
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

        // --- Configuração do RecyclerView ---
        ordersRecyclerView = findViewById(R.id.recycler_view_my_orders);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(this, orderList);
        ordersRecyclerView.setAdapter(orderAdapter);

        // Carrega os pedidos
        loadOrders();

        // --- Lógica do Menu de Navegação Inferior (ADICIONADA) ---
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_orders) {
                return true; // Não faz nada, já estamos aqui
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), VitrineActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_coupons) {
                startActivity(new Intent(getApplicationContext(), CouponsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void loadOrders() {
        // ATENÇÃO: Carregando todos os pedidos. Idealmente, filtrar por usuário logado.
        new LoadOrdersAsyncTask().execute();
    }

    private class LoadOrdersAsyncTask extends AsyncTask<Void, Void, List<Order>> {
        @Override
        protected List<Order> doInBackground(Void... voids) {
            // Supondo que o método para buscar todos os pedidos se chame getAllOrders(). Verifique seu OrderDAO.
            return db.orderDAO().getAllOrders(); 
        }

        @Override
        protected void onPostExecute(List<Order> orders) {
            if (orders != null) {
                orderList.clear();
                orderList.addAll(orders);
                orderAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Usa a navegação da BottomNavigationView em vez de voltar
        startActivity(new Intent(getApplicationContext(), VitrineActivity.class));
        overridePendingTransition(0, 0);
        finish();
        return true;
    }
}
