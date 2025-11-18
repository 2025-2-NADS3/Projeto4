package com.example.projeto_entrega2.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UsuarioDAO {
    @Insert
    void insert(Usuario usuario);

    @Query("SELECT * FROM tabela_de_usuarios")
    List<Usuario> getAll();

    @Query("SELECT * FROM tabela_de_usuarios WHERE email = :email AND senha = :senha LIMIT 1")
    Usuario login(String email, String senha);

    @Query("DELETE FROM tabela_de_usuarios")
    void deleteAll();
}
