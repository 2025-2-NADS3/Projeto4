package com.example.projeto_entrega2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.R;
import com.example.projeto_entrega2.model.CartItem;
import com.example.projeto_entrega2.model.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.OrderViewHolder> {

    private List<Order> orders = new ArrayList<>();

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_order, parent, false);
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
        private final TextView statusTextView, timestampTextView, itemsTextView, pickupCodeTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            statusTextView = itemView.findViewById(R.id.text_view_order_status);
            timestampTextView = itemView.findViewById(R.id.text_view_order_timestamp);
            itemsTextView = itemView.findViewById(R.id.text_view_order_items);
            pickupCodeTextView = itemView.findViewById(R.id.text_view_pickup_code);
        }

        public void bind(final Order order) {
            statusTextView.setText("Status: " + order.getStatus());
            pickupCodeTextView.setText(order.getPickupCode());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            timestampTextView.setText(sdf.format(new Date(order.getOrderTimestamp())));

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<CartItem>>(){}.getType();
            List<CartItem> items = gson.fromJson(order.getItemsJson(), listType);
            StringBuilder itemsBuilder = new StringBuilder();
            for (CartItem item : items) {
                itemsBuilder.append(String.format(Locale.getDefault(), "- %dx %s\n", item.getQuantity(), item.getProductName()));
            }
            itemsTextView.setText(itemsBuilder.toString().trim());
        }
    }
}
