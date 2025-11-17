package com.example.projeto_entrega2.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabela_de_usuarios", indices = {@Index(value = {"email"}, unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String email;
    private String password;
    private String profileType; // "ALUNO" ou "CANTINA"

    public User(String email, String password, String profileType) {
        this.email = email;
        this.password = password;
        this.profileType = profileType;
    }

    // --- Getters ---
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfileType() {
        return profileType;
    }

    // --- Setter para o ID (necessário para o Room) ---
    public void setId(int id) {
        this.id = id;
    }
}
