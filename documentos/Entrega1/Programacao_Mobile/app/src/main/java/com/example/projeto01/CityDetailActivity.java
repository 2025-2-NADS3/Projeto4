package com.example.projeto01;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projeto01.model.City;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CityDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CIDADE = "com.example.projeto01.EXTRA_CIDADE";

    private ImageView imageViewCityDetail;
    private TextView textViewCityNameDetail;
    private TextView textViewPopulationDetail;
    private TextView textViewAreaDetail;
    private TextView textViewDensityDetail;
    private Button botaoVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageViewCityDetail = findViewById(R.id.imageViewCityDetail);
        textViewCityNameDetail = findViewById(R.id.textViewCityNameDetail);
        textViewPopulationDetail = findViewById(R.id.textViewPopulationDetail);
        textViewAreaDetail = findViewById(R.id.textViewAreaDetail);
        textViewDensityDetail = findViewById(R.id.textViewDensityDetail);
        botaoVoltar = findViewById(R.id.buttonBack);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_CIDADE)) {
            City cidade = intent.getParcelableExtra(EXTRA_CIDADE);
            if (cidade != null) {
                preencherDetalhesDaCidade(cidade);
                if (actionBar != null) {
                    actionBar.setTitle(cidade.getNome());
                }
            }
        }

        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void preencherDetalhesDaCidade(City cidade) {
        textViewCityNameDetail.setText(cidade.getNome());

        NumberFormat populationFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        textViewPopulationDetail.setText(populationFormat.format(cidade.getPopulacao()));

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        textViewAreaDetail.setText(decimalFormat.format(cidade.getAreaKm2()));

        textViewDensityDetail.setText(decimalFormat.format(cidade.getDensidadePopulacional()));

        if (cidade.getIdRecursoImagem() != 0) {
            imageViewCityDetail.setImageResource(cidade.getIdRecursoImagem());
        } else {
            imageViewCityDetail.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
