package com.example.projeto_entrega2.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProdutoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Produto> produtos); // RENOMEADO DE inserirTodos PARA insertAll

    @Query("SELECT * FROM tabela_de_produtos ORDER BY nome ASC")
    List<Produto> getAll(); // RENOMEADO DE buscarTodos PARA getAll

    @Query("DELETE FROM tabela_de_produtos")
    void deleteAll(); // RENOMEADO DE apagarTodos PARA deleteAll
}
