package com.example.projeto_entrega2.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long userId;
    private String itemsJson; 
    private double totalAmount;
    private String status;
    private long orderTimestamp;
    private String pickupCode; // NOVO CAMPO: Código de retirada

    public Order() {}

    public Order(long userId, String itemsJson, double totalAmount, String status, long orderTimestamp, String pickupCode) {
        this.userId = userId;
        this.itemsJson = itemsJson;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderTimestamp = orderTimestamp;
        this.pickupCode = pickupCode;
    }

    // --- Getters e Setters ---

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getItemsJson() {
        return itemsJson;
    }

    public void setItemsJson(String itemsJson) {
        this.itemsJson = itemsJson;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(long orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public String getPickupCode() {
        return pickupCode;
    }

    public void setPickupCode(String pickupCode) {
        this.pickupCode = pickupCode;
    }
}
