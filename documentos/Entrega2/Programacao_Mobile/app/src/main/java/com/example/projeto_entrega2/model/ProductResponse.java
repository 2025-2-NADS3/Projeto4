package com.example.projeto_entrega2.model;

import java.util.List;

// Esta classe é um wrapper para a resposta da API DummyJSON.
// A API retorna um objeto JSON que contém uma lista de produtos sob a chave "products".
public class ProductResponse {

    private List<Produto> products;

    public List<Produto> getProducts() {
        return products;
    }

    public void setProducts(List<Produto> products) {
        this.products = products;
    }
}
