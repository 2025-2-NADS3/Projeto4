package com.example.projeto_entrega2;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projeto_entrega2.model.Produto;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Produto produto = getIntent().getParcelableExtra(EXTRA_PRODUCT);

        if (produto != null) {
            CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(produto.getNome());

            ImageView imageView = findViewById(R.id.detail_product_image);
            TextView priceTextView = findViewById(R.id.detail_product_price);
            TextView descriptionTextView = findViewById(R.id.detail_product_description);

            // Carrega a imagem
            if (!TextUtils.isEmpty(produto.getUrlImagem())) {
                Picasso.get()
                        .load(produto.getUrlImagem())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }

            // Formata e exibe o preço
            Locale ptBr = new Locale("pt", "BR");
            NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(ptBr);
            priceTextView.setText(formatoMoeda.format(produto.getPreco()));

            // Exibe a descrição
            descriptionTextView.setText(produto.getDescricao());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
