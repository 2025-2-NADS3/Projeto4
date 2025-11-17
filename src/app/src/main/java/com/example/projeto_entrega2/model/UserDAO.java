package com.example.projeto_entrega2.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDAO {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM tabela_de_usuarios WHERE email = :email LIMIT 1")
    User findByEmail(String email);

}
