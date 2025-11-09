package com.example.projeto_entrega2.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProdutoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Produto produto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Produto> produtos);

    @Query("SELECT * FROM tabela_de_produtos")
    List<Produto> getAll();

    @Query("DELETE FROM tabela_de_produtos")
    void deleteAll();
}
