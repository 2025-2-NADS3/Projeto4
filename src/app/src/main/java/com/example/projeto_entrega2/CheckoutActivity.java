package com.example.projeto_entrega2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projeto_entrega2.database.AppDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    public static final String EXTRA_TOTAL_AMOUNT = "EXTRA_TOTAL_AMOUNT";
    private AppDatabase db;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = AppDatabase.getDatabase(getApplicationContext());
        totalAmount = getIntent().getDoubleExtra(EXTRA_TOTAL_AMOUNT, 0.0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pagamento");
        }

        TextView totalTextView = findViewById(R.id.text_view_total_checkout);
        Locale ptBr = new Locale("pt", "BR");
        totalTextView.setText("Total: " + NumberFormat.getCurrencyInstance(ptBr).format(totalAmount));

        RadioGroup paymentRadioGroup = findViewById(R.id.radio_group_payment_checkout);
        Button confirmOrderButton = findViewById(R.id.button_confirm_order);

        confirmOrderButton.setOnClickListener(v -> {
            if (paymentRadioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Por favor, selecione um método de pagamento", Toast.LENGTH_SHORT).show();
                return;
            }
            new FinalizeOrderAsyncTask().execute();
        });
    }

    private class FinalizeOrderAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            db.cartItemDAO().deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(CheckoutActivity.this, "Pedido confirmado com sucesso!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CheckoutActivity.this, VitrineActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
