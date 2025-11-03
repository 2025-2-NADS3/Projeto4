package com.example.projeto_entrega2.network;

import com.example.projeto_entrega2.model.ProductResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    // ENDPOINT ATUALIZADO PARA A API PÚBLICA E ESTÁVEL DUMMYJSON
    @GET("products")
    Call<ProductResponse> getCardapio();

}
