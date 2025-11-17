package com.example.projeto_entrega2.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabela_de_produtos")
public class Produto implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nome;
    private double preco;
    private String descricao;
    private String categoria;
    private String marca;
    private String urlImagem;

    public Produto(String nome, double preco, String descricao, String categoria, String marca, String urlImagem) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.categoria = categoria;
        this.marca = marca;
        this.urlImagem = urlImagem;
    }

    protected Produto(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        preco = in.readDouble();
        descricao = in.readString();
        categoria = in.readString();
        marca = in.readString();
        urlImagem = in.readString();
    }

    public static final Creator<Produto> CREATOR = new Creator<Produto>() {
        @Override
        public Produto createFromParcel(Parcel in) {
            return new Produto(in);
        }

        @Override
        public Produto[] newArray(int size) {
            return new Produto[size];
        }
    };

    // --- Getters ---
    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public String getMarca() { return marca; }
    public String getUrlImagem() { return urlImagem; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeDouble(preco);
        dest.writeString(descricao);
        dest.writeString(categoria);
        dest.writeString(marca);
        dest.writeString(urlImagem);
    }
}
