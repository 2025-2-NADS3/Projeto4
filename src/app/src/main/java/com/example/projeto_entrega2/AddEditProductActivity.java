package com.example.projeto_entrega2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.Produto;

public class AddEditProductActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextName, editTextDescription, editTextPrice;
    private Spinner spinnerCategory;
    private Button buttonSave, buttonSelectImage;
    private ImageView imageViewPreview;
    private AppDatabase db;
    private Uri imageUri;
    private Produto currentProduct; // Produto a ser editado

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openGallery();
                } else {
                    Toast.makeText(this, "Permissão de acesso à galeria negada", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                    imageUri = result.getData().getData();
                    final int takeFlags = result.getData().getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
                    imageViewPreview.setImageURI(imageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        db = AppDatabase.getDatabase(getApplicationContext());
        initViews();
        setupSpinner();

        // Verifica se está em modo de edição
        if (getIntent().hasExtra(EXTRA_PRODUCT)) {
            currentProduct = getIntent().getParcelableExtra(EXTRA_PRODUCT);
            setTitle("Editar Produto");
            populateFields();
        } else {
            setTitle("Adicionar Produto");
        }

        buttonSelectImage.setOnClickListener(v -> checkPermissionAndOpenGallery());
        buttonSave.setOnClickListener(v -> saveProduct());
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        imageViewPreview = findViewById(R.id.image_view_product_preview);
        buttonSelectImage = findViewById(R.id.button_select_image);
        editTextName = findViewById(R.id.edit_text_product_name);
        editTextDescription = findViewById(R.id.edit_text_product_description);
        editTextPrice = findViewById(R.id.edit_text_product_price);
        spinnerCategory = findViewById(R.id.spinner_category);
        buttonSave = findViewById(R.id.button_save);
    }

    private void populateFields() {
        editTextName.setText(currentProduct.getNome());
        editTextDescription.setText(currentProduct.getDescricao());
        editTextPrice.setText(String.valueOf(currentProduct.getPreco()));

        if (!TextUtils.isEmpty(currentProduct.getUrlImagem())) {
            imageUri = Uri.parse(currentProduct.getUrlImagem());
            imageViewPreview.setImageURI(imageUri);
        }

        // Seleciona a categoria no spinner
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerCategory.getAdapter();
        int position = adapter.getPosition(currentProduct.getCategoria());
        spinnerCategory.setSelection(position);
    }

    private void checkPermissionAndOpenGallery() {
        String permission = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;
        requestPermissionLauncher.launch(permission);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupSpinner() {
        String[] categories = {"Salgados", "Doces", "Bebidas", "Refeições"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);
    }

    private void saveProduct() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String imageUrl = (imageUri != null) ? imageUri.toString() : "";

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Nome e preço são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Por favor, selecione uma imagem", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        
        Produto productToSave = (currentProduct != null) ? currentProduct : new Produto(name, price, description, category, "", imageUrl);
        if (currentProduct != null) { // Atualiza os campos se estiver editando
            productToSave = new Produto(name, price, description, category, "", imageUrl);
            productToSave.setId(currentProduct.getId());
        }

        new SaveProductAsyncTask().execute(productToSave);
    }

    private class SaveProductAsyncTask extends AsyncTask<Produto, Void, Void> {
        @Override
        protected Void doInBackground(Produto... produtos) {
            Produto produto = produtos[0];
            if (produto.getId() == 0) {
                db.produtoDAO().insert(produto);
            } else {
                db.produtoDAO().update(produto);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(AddEditProductActivity.this, "Produto salvo!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
    }
}
