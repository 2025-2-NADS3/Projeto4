package com.example.projeto_entrega2;

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

import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.CartItem;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.CarrinhoViewHolder> {

    private List<CartItem> items = new ArrayList<>();
    private final Consumer<List<CartItem>> updateTotalCallback;
    private final Context context;

    public CarrinhoAdapter(Context context, Consumer<List<CartItem>> updateTotalCallback) {
        this.context = context;
        this.updateTotalCallback = updateTotalCallback;
    }

    @NonNull
    @Override
    public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_carrinho, parent, false);
        return new CarrinhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoViewHolder holder, int position) {
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

    class CarrinhoViewHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImage;
        private final TextView itemName;
        private final TextView itemPrice;
        private final TextView itemQuantity;
        private final ImageButton deleteButton;

        public CarrinhoViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.carrinho_item_image);
            itemName = itemView.findViewById(R.id.carrinho_item_name);
            itemPrice = itemView.findViewById(R.id.carrinho_item_price);
            itemQuantity = itemView.findViewById(R.id.carrinho_item_quantity);
            deleteButton = itemView.findViewById(R.id.carrinho_item_delete);
        }

        public void bind(final CartItem cartItem) {
            itemName.setText(cartItem.getProductName());
            itemQuantity.setText("Qtd: " + cartItem.getQuantity());

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
                    CartItem itemToDelete = items.get(position);
                    deleteItemFromDb(itemToDelete, position);
                }
            });
        }

        private void deleteItemFromDb(final CartItem cartItem, final int position) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    AppDatabase.getDatabase(context).cartItemDAO().delete(cartItem);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    items.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, items.size());
                    updateTotalCallback.accept(items);
                }
            }.execute();
        }
    }
}
