package com.example.projeto_entrega2.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabela_carrinho")
public class CartItem {

    @PrimaryKey
    private int productId;

    private String productName;
    private double productPrice;
    private String productImageUrl;
    private int quantity;

    // Construtor, getters e setters

    public CartItem(int productId, String productName, double productPrice, String productImageUrl, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
