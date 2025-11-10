package com.example.projeto_entrega2;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projeto_entrega2.model.Produto;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;
    private RequestQueue requestQueue;
    private static final String URL = "https://dummyjson.com/products?limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);
        requestQueue = Volley.newRequestQueue(this);

        loadProductsAndCreateCharts();
    }

    private void loadProductsAndCreateCharts() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL, null,
                response -> {
                    try {
                        String productsJson = response.getJSONArray("products").toString();
                        Type listType = new TypeToken<List<Produto>>() {}.getType();
                        List<Produto> produtos = new Gson().fromJson(productsJson, listType);
                        processDataForCharts(produtos);
                    } catch (JSONException e) {
                        Toast.makeText(AnalyticsActivity.this, "Error parsing JSON", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(AnalyticsActivity.this, "API communication failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void processDataForCharts(List<Produto> produtos) {
        // Estruturas para armazenar dados agregados por marca
        Map<String, Integer> brandProductCount = new HashMap<>();
        Map<String, Double> brandTotalPrice = new HashMap<>();

        for (Produto p : produtos) {
            // O campo 'brand' precisa existir no seu GSON/Produto.java
            String brand = p.getMarca() != null ? p.getMarca() : "Unknown"; 

            // Contagem de produtos por marca
            brandProductCount.put(brand, brandProductCount.getOrDefault(brand, 0) + 1);

            // Soma dos preços por marca
            brandTotalPrice.put(brand, brandTotalPrice.getOrDefault(brand, 0.0) + p.getPreco());
        }

        setupPieChart(brandProductCount);
        setupBarChart(brandProductCount, brandTotalPrice);
    }

    private void setupPieChart(Map<String, Integer> brandProductCount) {
        List<PieEntry> pieEntries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : brandProductCount.entrySet()) {
            pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(12f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Produtos por Marca");
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void setupBarChart(Map<String, Integer> brandProductCount, Map<String, Double> brandTotalPrice) {
        List<BarEntry> barEntries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Double> entry : brandTotalPrice.entrySet()) {
            String brand = entry.getKey();
            double total = entry.getValue();
            int count = brandProductCount.getOrDefault(brand, 1); // Evita divisão por zero
            float avgPrice = (float) (total / count);

            barEntries.add(new BarEntry(index, avgPrice));
            labels.add(brand);
            index++;
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Preço Médio ($)");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.6f);

        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(-45);

        barChart.invalidate(); // refresh
    }
}
