package com.example.projeto_entrega2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.R;
import com.example.projeto_entrega2.model.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final Context context;
    private final List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView orderIdTextView;
        private final TextView orderTotalTextView;
        private final TextView orderStatusTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.text_view_order_id);
            orderTotalTextView = itemView.findViewById(R.id.text_view_order_total);
            orderStatusTextView = itemView.findViewById(R.id.text_view_order_status);
        }

        public void bind(Order order) {
            orderIdTextView.setText(String.valueOf(order.getId()));
            orderStatusTextView.setText(order.getStatus());

            // Formata o valor total como moeda brasileira
            Locale ptBr = new Locale("pt", "BR");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(ptBr);
            orderTotalTextView.setText(currencyFormat.format(order.getTotalAmount()));
        }
    }
}
