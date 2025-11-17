package com.example.projeto_entrega2.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartItemDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CartItem item);

    @Update
    void update(CartItem item);

    @Delete
    void delete(CartItem item);

    @Query("SELECT * FROM tabela_carrinho")
    List<CartItem> getAllItems();

    @Query("SELECT * FROM tabela_carrinho WHERE productId = :productId LIMIT 1")
    CartItem findById(int productId);

    @Query("DELETE FROM tabela_carrinho")
    void deleteAll();
}
