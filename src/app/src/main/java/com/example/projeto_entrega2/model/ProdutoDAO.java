package com.example.projeto_entrega2.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProdutoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Produto produto);

    @Update
    void update(Produto produto);

    @Delete
    void delete(Produto produto);

    @Query("SELECT * FROM tabela_de_produtos ORDER BY nome")
    List<Produto> getAll();

    @Query("SELECT * FROM tabela_de_produtos WHERE id = :produtoId LIMIT 1")
    Produto findById(int produtoId);

    @Query("SELECT * FROM tabela_de_produtos WHERE categoria = :categoria ORDER BY nome")
    List<Produto> getProductsByCategory(String categoria);

    @Query("DELETE FROM tabela_de_produtos")
    void deleteAll();
}
