package com.example.projeto01.model;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {
    private String nome;
    private long populacao;
    private double areaKm2;
    private int idRecursoImagem;

    public City(String nome, long populacao, double areaKm2, int idRecursoImagem) {
        this.nome = nome;
        this.populacao = populacao;
        this.areaKm2 = areaKm2;
        this.idRecursoImagem = idRecursoImagem;
    }

    protected City(Parcel in) {
        nome = in.readString();
        populacao = in.readLong();
        areaKm2 = in.readDouble();
        idRecursoImagem = in.readInt();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public long getPopulacao() {
        return populacao;
    }

    public double getAreaKm2() {
        return areaKm2;
    }

    public int getIdRecursoImagem() {
        return idRecursoImagem;
    }

    public double getDensidadePopulacional() {
        if (areaKm2 == 0) {
            return 0;
        }
        return populacao / areaKm2;
    }

    @Override
    public String toString() {
        return "City{" +
                "nome='" + nome + '\'' +
                ", populacao=" + populacao +
                ", areaKm2=" + areaKm2 +
                ", idRecursoImagem=" + idRecursoImagem +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeLong(populacao);
        dest.writeDouble(areaKm2);
        dest.writeInt(idRecursoImagem);
    }
}
