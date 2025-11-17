package com.example.projeto_entrega2.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabela_de_usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nomeCompleto;
    private String nomeUsuario;
    private String email;
    private String senha;
    private boolean isCantina; // true se for admin/cantina, false se for cliente

    // Construtor
    public Usuario(String nomeCompleto, String nomeUsuario, String email, String senha, boolean isCantina) {
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.isCantina = isCantina;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public boolean isCantina() {
        return isCantina;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }
}
