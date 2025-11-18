package com.example.projeto_entrega2.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.R;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.CartItem;
import com.example.projeto_entrega2.model.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderManagementAdapter extends RecyclerView.Adapter<OrderManagementAdapter.OrderViewHolder> {

    private List<Order> orders = new ArrayList<>();
    private final Context context;

    public OrderManagementAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_manage_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order currentOrder = orders.get(position);
        holder.bind(currentOrder);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView idTimestampTextView, statusTextView, itemsTextView, totalTextView;
        private final Button startOrderButton, finalizeOrderButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            idTimestampTextView = itemView.findViewById(R.id.text_view_order_id_timestamp);
            statusTextView = itemView.findViewById(R.id.text_view_order_status);
            itemsTextView = itemView.findViewById(R.id.text_view_order_items);
            totalTextView = itemView.findViewById(R.id.text_view_order_total);
            startOrderButton = itemView.findViewById(R.id.button_start_order);
            finalizeOrderButton = itemView.findViewById(R.id.button_finalize_order);
        }

        public void bind(final Order order) {
            // Preenche os dados básicos
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            idTimestampTextView.setText(String.format(Locale.getDefault(), "Pedido #%d - %s", order.getId(), sdf.format(new Date(order.getOrderTimestamp()))));
            statusTextView.setText("Status: " + order.getStatus());
            totalTextView.setText("Total: " + NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(order.getTotalAmount()));

            // Preenche os itens
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<CartItem>>(){}.getType();
            List<CartItem> items = gson.fromJson(order.getItemsJson(), listType);
            StringBuilder itemsBuilder = new StringBuilder();
            for (CartItem item : items) {
                itemsBuilder.append(String.format(Locale.getDefault(), "- %dx %s\n", item.getQuantity(), item.getProductName()));
            }
            itemsTextView.setText(itemsBuilder.toString().trim());

            // Lógica de visibilidade dos botões
            switch (order.getStatus()) {
                case "Pendente":
                    startOrderButton.setVisibility(View.VISIBLE);
                    finalizeOrderButton.setVisibility(View.GONE);
                    break;
                case "Em Preparo":
                    startOrderButton.setVisibility(View.GONE);
                    finalizeOrderButton.setVisibility(View.VISIBLE);
                    break;
                default: // "Pronto para Retirada", "Entregue"
                    startOrderButton.setVisibility(View.GONE);
                    finalizeOrderButton.setVisibility(View.GONE);
                    break;
            }

            // Ações dos botões
            startOrderButton.setOnClickListener(v -> {
                updateOrderStatus(order, "Em Preparo");
            });

            finalizeOrderButton.setOnClickListener(v -> {
                showFinalizeDialog(order);
            });
        }
        
        private void showFinalizeDialog(Order order) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Finalizar Pedido #" + order.getId());
            builder.setMessage("Digite o código de retirada do cliente:");

            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            builder.setView(input);

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                String code = input.getText().toString().toUpperCase().trim();
                if (code.equals(order.getPickupCode())) {
                    updateOrderStatus(order, "Entregue");
                } else {
                    Toast.makeText(context, "Código incorreto!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

            builder.show();
        }

        private void updateOrderStatus(Order order, String newStatus) {
            order.setStatus(newStatus);
            new UpdateOrderStatusAsyncTask().execute(order);
        }
    }

    private class UpdateOrderStatusAsyncTask extends AsyncTask<Order, Void, Order> {
        @Override
        protected Order doInBackground(Order... orders) {
            if (orders.length > 0) {
                AppDatabase.getDatabase(context).orderDAO().update(orders[0]);
                return orders[0];
            }
            return null;
        }

        @Override
        protected void onPostExecute(Order updatedOrder) {
            if (updatedOrder != null) {
                // Encontra a posição do pedido e notifica a mudança
                for (int i = 0; i < orders.size(); i++) {
                    if (orders.get(i).getId() == updatedOrder.getId()) {
                        orders.set(i, updatedOrder);
                        notifyItemChanged(i);
                        break;
                    }
                }
                Toast.makeText(context, "Status atualizado para: " + updatedOrder.getStatus(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
