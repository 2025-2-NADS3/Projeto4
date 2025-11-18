package com.example.projeto_entrega2.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.R;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.CartItem;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> items = new ArrayList<>();
    private final Consumer<List<CartItem>> updateTotalCallback;
    private final Context context;

    public CartAdapter(Context context, Consumer<List<CartItem>> updateTotalCallback) {
        this.context = context;
        this.updateTotalCallback = updateTotalCallback;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_carrinho, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem currentItem = items.get(position);
        holder.bind(currentItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<CartItem> cartItems) {
        this.items = cartItems;
        notifyDataSetChanged();
        updateTotalCallback.accept(items);
    }

    private void updateItem(CartItem item) {
        new UpdateCartItemAsyncTask().execute(item);
    }

    private void deleteItem(CartItem item, int position) {
        new DeleteCartItemAsyncTask(position).execute(item);
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImage;
        private final TextView itemName, itemPrice, itemQuantity;
        private final ImageButton increaseButton, decreaseButton, deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.carrinho_item_image);
            itemName = itemView.findViewById(R.id.carrinho_item_name);
            itemPrice = itemView.findViewById(R.id.carrinho_item_price);
            itemQuantity = itemView.findViewById(R.id.carrinho_item_quantity);
            increaseButton = itemView.findViewById(R.id.button_increase_quantity);
            decreaseButton = itemView.findViewById(R.id.button_decrease_quantity);
            deleteButton = itemView.findViewById(R.id.carrinho_item_delete);
        }

        public void bind(final CartItem cartItem) {
            itemName.setText(cartItem.getProductName());
            itemQuantity.setText(String.valueOf(cartItem.getQuantity()));

            Locale ptBr = new Locale("pt", "BR");
            NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(ptBr);
            itemPrice.setText(formatoMoeda.format(cartItem.getPrice() * cartItem.getQuantity()));

            if (cartItem.getImageUrl() != null && !cartItem.getImageUrl().isEmpty()) {
                Picasso.get().load(cartItem.getImageUrl()).into(itemImage);
            } else {
                itemImage.setImageResource(R.drawable.ic_launcher_background);
            }

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    deleteItem(items.get(position), position);
                }
            });

            increaseButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CartItem item = items.get(position);
                    item.setQuantity(item.getQuantity() + 1);
                    updateItem(item);
                    notifyItemChanged(position);
                    updateTotalCallback.accept(items);
                }
            });

            decreaseButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CartItem item = items.get(position);
                    if (item.getQuantity() > 1) {
                        item.setQuantity(item.getQuantity() - 1);
                        updateItem(item);
                        notifyItemChanged(position);
                        updateTotalCallback.accept(items);
                    } else {
                        // Se a quantidade é 1, remover o item
                        deleteItem(item, position);
                    }
                }
            });
        }
    }

    private class UpdateCartItemAsyncTask extends AsyncTask<CartItem, Void, Void> {
        @Override
        protected Void doInBackground(CartItem... items) {
            if (items.length > 0) {
                AppDatabase.getDatabase(context).cartItemDAO().update(items[0]);
            }
            return null;
        }
    }

    private class DeleteCartItemAsyncTask extends AsyncTask<CartItem, Void, Void> {
        private int position;

        DeleteCartItemAsyncTask(int position) {
            this.position = position;
        }

        @Override
        protected Void doInBackground(CartItem... items) {
            if (items.length > 0) {
                AppDatabase.getDatabase(context).cartItemDAO().delete(items[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (position < items.size()) {
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());
                updateTotalCallback.accept(items);
            }
        }
    }
}
