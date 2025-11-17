package com.example.projeto_entrega2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.CartItem;
import com.example.projeto_entrega2.model.Produto;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";
    private Produto currentProduct;
    private AppDatabase db;

    private TextView quantityTextView;
    private Button addToCartButton;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = AppDatabase.getDatabase(getApplicationContext());
        currentProduct = getIntent().getParcelableExtra(EXTRA_PRODUCT);

        initViews();
        setupToolbar();

        if (currentProduct != null) {
            populateProductData();
            setupQuantityControls();
            updateAddToCartButton();
        }
    }

    private void initViews() {
        quantityTextView = findViewById(R.id.text_view_quantity);
        addToCartButton = findViewById(R.id.button_add_to_cart);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(""); // Título da barra de ferramentas fica em branco
        }
    }

    private void populateProductData() {
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(""); // Título da barra que recolhe também fica em branco

        // NOVOS CAMPOS
        TextView nameTextView = findViewById(R.id.detail_product_name);
        nameTextView.setText(currentProduct.getNome());

        ImageView imageView = findViewById(R.id.detail_product_image);
        TextView priceTextView = findViewById(R.id.detail_product_price);
        TextView descriptionTextView = findViewById(R.id.detail_product_description);

        if (!TextUtils.isEmpty(currentProduct.getUrlImagem())) {
            Picasso.get().load(currentProduct.getUrlImagem()).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }

        Locale ptBr = new Locale("pt", "BR");
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(ptBr);
        priceTextView.setText(formatoMoeda.format(currentProduct.getPreco()));
        descriptionTextView.setText(currentProduct.getDescricao());
    }

    private void setupQuantityControls() {
        ImageButton increaseButton = findViewById(R.id.button_increase_quantity);
        ImageButton decreaseButton = findViewById(R.id.button_decrease_quantity);

        increaseButton.setOnClickListener(v -> {
            quantity++;
            quantityTextView.setText(String.valueOf(quantity));
            updateAddToCartButton();
        });

        decreaseButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityTextView.setText(String.valueOf(quantity));
                updateAddToCartButton();
            }
        });

        addToCartButton.setOnClickListener(v -> addToCart());
    }

    private void updateAddToCartButton() {
        double totalPrice = currentProduct.getPreco() * quantity;
        Locale ptBr = new Locale("pt", "BR");
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(ptBr);
        String buttonText = "Adicionar " + formatoMoeda.format(totalPrice);
        addToCartButton.setText(buttonText);
    }

    private void addToCart() {
        new AddToCartAsyncTask().execute(currentProduct);
    }

    private class AddToCartAsyncTask extends AsyncTask<Produto, Void, Void> {
        @Override
        protected Void doInBackground(Produto... produtos) {
            Produto produto = produtos[0];
            CartItem existingItem = db.cartItemDAO().findById(produto.getId());

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                db.cartItemDAO().update(existingItem);
            } else {
                CartItem newItem = new CartItem(produto.getId(), produto.getNome(), produto.getPreco(), produto.getUrlImagem(), quantity);
                db.cartItemDAO().insert(newItem);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(ProductDetailActivity.this, quantity + " item(s) adicionado(s)!", Toast.LENGTH_SHORT).show();
            finish(); // Volta para a lista de produtos
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
