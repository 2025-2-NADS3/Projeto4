package com.example.projeto_entrega2.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_entrega2.R;
import com.example.projeto_entrega2.model.Produto;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    private List<Produto> listaProdutos;
    private OnItemClickListener listener;
    private Context context;

    // CONSTRUTOR CORRIGIDO: Aceita a lista e o contexto
    public ProdutoAdapter(Context context, List<Produto> productList) {
        this.context = context;
        this.listaProdutos = productList;
    }

    // Construtor antigo mantido para compatibilidade
    public ProdutoAdapter() {
        this.listaProdutos = new ArrayList<>();
    }

    public interface OnItemClickListener {
        void onItemClick(Produto produto);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_produto, parent, false);
        return new ProdutoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Produto produtoAtual = listaProdutos.get(position);
        holder.textViewNome.setText(produtoAtual.getNome());

        Locale ptBr = new Locale("pt", "BR");
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(ptBr);
        holder.textViewPreco.setText(formatoMoeda.format(produtoAtual.getPreco()));

        if (!TextUtils.isEmpty(produtoAtual.getUrlImagem())) {
            Picasso.get()
                    .load(produtoAtual.getUrlImagem())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.imageViewProduto);
        } else {
            holder.imageViewProduto.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return listaProdutos != null ? listaProdutos.size() : 0;
    }

    public void setProdutos(List<Produto> produtos) {
        this.listaProdutos = produtos;
        notifyDataSetChanged();
    }

    public Produto getProductAt(int position) {
        return listaProdutos.get(position);
    }

    class ProdutoViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewProduto;
        private final TextView textViewNome;
        private final TextView textViewPreco;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduto = itemView.findViewById(R.id.imageViewProduto);
            textViewNome = itemView.findViewById(R.id.textViewNomeProduto);
            textViewPreco = itemView.findViewById(R.id.textViewPrecoProduto);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(listaProdutos.get(position));
                }
            });
        }
    }
}
