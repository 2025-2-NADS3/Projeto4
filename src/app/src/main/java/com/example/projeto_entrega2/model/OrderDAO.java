package com.example.projeto_entrega2.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDAO {

    @Insert
    long insert(Order order);

    @Update
    void update(Order order);

    @Query("SELECT * FROM orders ORDER BY orderTimestamp DESC")
    List<Order> getAllOrders();

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderTimestamp DESC")
    List<Order> getOrdersByUserId(long userId);

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY orderTimestamp DESC")
    List<Order> getOrdersByStatus(String status);

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    Order getOrderById(long orderId);
}
