package com.example.projeto_entrega2.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tabela_de_produtos")
public class Produto implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") // Mapeamento para o campo 'id' da API
    private int id;

    @SerializedName("title") // Mapeamento para o campo 'title' da API
    private String nome;

    @SerializedName("price") // Mapeamento para o campo 'price' da API
    private double preco;

    @SerializedName("description") // Mapeamento para o campo 'description' da API
    private String descricao;

    @SerializedName("category") // Mapeamento para o campo 'category' da API
    private String categoria;

    @SerializedName("thumbnail") // Mapeamento para o campo 'thumbnail' da API
    private String urlImagem;

    public Produto(String nome, double preco, String descricao, String categoria, String urlImagem) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.categoria = categoria;
        this.urlImagem = urlImagem;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public String getUrlImagem() { return urlImagem; }

    // --- Setter para o ID ---
    public void setId(int id) {
        this.id = id;
    }

    // Implementação do Parcelable (necessária para passar objetos entre Activities, não usada aqui, mas mantida por segurança)
    protected Produto(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        preco = in.readDouble();
        descricao = in.readString();
        categoria = in.readString();
        urlImagem = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeDouble(preco);
        dest.writeString(descricao);
        dest.writeString(categoria);
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
