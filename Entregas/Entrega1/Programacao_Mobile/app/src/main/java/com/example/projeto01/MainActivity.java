package com.example.projeto01;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projeto01.adapter.CityAdapter;
import com.example.projeto01.model.City;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CityAdapter.OuvinteCliqueItem {

    private RecyclerView recyclerViewCidades;
    private CityAdapter cidadeAdaptador;
    private List<City> listaCidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewCidades = findViewById(R.id.recyclerViewCities);
        recyclerViewCidades.setLayoutManager(new LinearLayoutManager(this));

        listaCidades = new ArrayList<>();
        listaCidades.add(new City("São Paulo", 12396372, 1521, R.drawable.sao_paulo));
        listaCidades.add(new City("Rio de Janeiro", 6775561, 1200, R.drawable.rio_de_janeiro));
        listaCidades.add(new City("Belo Horizonte", 2530701, 331, R.drawable.belo_horizonte));
        listaCidades.add(new City("Brasília", 3094325, 5760, R.drawable.brasilia));
        listaCidades.add(new City("Salvador", 2900319, 693, R.drawable.salvador));

        cidadeAdaptador = new CityAdapter(listaCidades);
        recyclerViewCidades.setAdapter(cidadeAdaptador);
        cidadeAdaptador.definirOuvinteCliqueItem(this);
    }

    @Override
    public void aoClicarItem(City cidade) {
        Intent intent = new Intent(MainActivity.this, CityDetailActivity.class);
        intent.putExtra(CityDetailActivity.EXTRA_CIDADE, cidade);
        startActivity(intent);
    }
}
