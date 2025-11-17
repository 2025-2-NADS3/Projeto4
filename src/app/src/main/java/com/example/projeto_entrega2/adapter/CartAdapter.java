package com.example.projeto_entrega2.adapter;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.R;
import com.example.projeto_entrega2.model.CartItem;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems = new ArrayList<>();
    private OnCartItemInteractionListener listener;

    public interface OnCartItemInteractionListener {
        void onIncreaseQuantity(CartItem item);
        void onDecreaseQuantity(CartItem item);
        void onRemoveItem(CartItem item);
    }

    public CartAdapter(OnCartItemInteractionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_carrinho, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem currentItem = cartItems.get(position);

        holder.itemName.setText(currentItem.getProductName());
        holder.itemQuantity.setText(String.valueOf(currentItem.getQuantity()));

        Locale ptBr = new Locale("pt", "BR");
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(ptBr);
        double totalPrice = currentItem.getProductPrice() * currentItem.getQuantity();
        holder.itemPrice.setText(formatoMoeda.format(totalPrice));

        if (!TextUtils.isEmpty(currentItem.getProductImageUrl())) {
            Picasso.get().load(Uri.parse(currentItem.getProductImageUrl())).into(holder.itemImage);
        } else {
            holder.itemImage.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.increaseButton.setOnClickListener(v -> listener.onIncreaseQuantity(currentItem));
        holder.decreaseButton.setOnClickListener(v -> listener.onDecreaseQuantity(currentItem));
        holder.removeButton.setOnClickListener(v -> listener.onRemoveItem(currentItem));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemPrice, itemQuantity;
        ImageButton increaseButton, decreaseButton, removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.cart_item_image);
            itemName = itemView.findViewById(R.id.cart_item_name);
            itemPrice = itemView.findViewById(R.id.cart_item_price);
            itemQuantity = itemView.findViewById(R.id.cart_item_quantity);
            increaseButton = itemView.findViewById(R.id.button_increase);
            decreaseButton = itemView.findViewById(R.id.button_decrease);
            removeButton = itemView.findViewById(R.id.button_remove);
        }
    }
}
