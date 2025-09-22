package com.example.projeto01.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projeto01.R;
import com.example.projeto01.model.City;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CidadeViewHolder> {

    private List<City> listaCidades;
    private OuvinteCliqueItem ouvinteCliqueItem;

    public interface OuvinteCliqueItem {
        void aoClicarItem(City cidade);
    }

    public CityAdapter(List<City> listaCidades) {
        this.listaCidades = listaCidades;
    }

    @NonNull
    @Override
    public CidadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_city, parent, false);
        return new CidadeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CidadeViewHolder holder, int position) {
        City cidadeAtual = listaCidades.get(position);
        holder.textViewCityName.setText(cidadeAtual.getNome());

        NumberFormat populationFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String populacaoFormatada = populationFormat.format(cidadeAtual.getPopulacao());
        holder.textViewCityPopulation.setText("População: " + populacaoFormatada);

        if (cidadeAtual.getIdRecursoImagem() != 0) {
            holder.imageViewCity.setImageResource(cidadeAtual.getIdRecursoImagem());
        } else {
            holder.imageViewCity.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            if (ouvinteCliqueItem != null) {
                ouvinteCliqueItem.aoClicarItem(cidadeAtual);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCidades == null ? 0 : listaCidades.size();
    }

    public void definirCidades(List<City> cidades) {
        this.listaCidades = cidades;
        notifyDataSetChanged();
    }

    static class CidadeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCity;
        TextView textViewCityName;
        TextView textViewCityPopulation;

        CidadeViewHolder(View itemView) {
            super(itemView);
            imageViewCity = itemView.findViewById(R.id.imageViewCity);
            textViewCityName = itemView.findViewById(R.id.textViewCityName);
            textViewCityPopulation = itemView.findViewById(R.id.textViewCityPopulation);
        }
    }

    public void definirOuvinteCliqueItem(OuvinteCliqueItem listener) {
        this.ouvinteCliqueItem = listener;
    }
}
