package com.example.projeto_entrega2.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tabela_de_produtos")
public class Produto implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String nome;

    @SerializedName("price")
    private double preco;

    @SerializedName("description")
    private String descricao;

    @SerializedName("category")
    private String categoria;

    @SerializedName("brand") // NOVO CAMPO
    private String marca;

    @SerializedName("thumbnail")
    private String urlImagem;

    // CONSTRUTOR ATUALIZADO
    public Produto(String nome, double preco, String descricao, String categoria, String marca, String urlImagem) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.categoria = categoria;
        this.marca = marca;
        this.urlImagem = urlImagem;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public String getMarca() { return marca; } // NOVO GETTER
    public String getUrlImagem() { return urlImagem; }

    // --- Setter para o ID ---
    public void setId(int id) {
        this.id = id;
    }

    // PARCELABLE ATUALIZADO
    protected Produto(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        preco = in.readDouble();
        descricao = in.readString();
        categoria = in.readString();
        marca = in.readString(); // NOVO CAMPO
        urlImagem = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeDouble(preco);
        dest.writeString(descricao);
        dest.writeString(categoria);
        dest.writeString(marca); // NOVO CAMPO
        dest.writeString(urlImagem);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
