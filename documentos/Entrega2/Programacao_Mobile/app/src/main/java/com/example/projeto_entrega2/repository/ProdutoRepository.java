package com.example.projeto_entrega2.repository;

import android.content.Context;
import android.os.AsyncTask;
import com.example.projeto_entrega2.database.AppDatabase;
import com.example.projeto_entrega2.model.ProductResponse;
import com.example.projeto_entrega2.model.Produto;
import com.example.projeto_entrega2.model.ProdutoDAO;
import com.example.projeto_entrega2.network.ApiClient;
import com.example.projeto_entrega2.network.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoRepository {

    private ProdutoDAO produtoDAO;
    private ApiService apiService;

    public interface RepositoryCallback<T> {
        void onComplete(T result);
    }

    public ProdutoRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        produtoDAO = db.produtoDAO();
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    // MÉTODO CORRIGIDO PARA USAR A LÓGICA ASSÍNCRONA E PROCESSAR A RESPOSTA CORRETA
    public void buscarProdutos(final RepositoryCallback<List<Produto>> callback) {
        apiService.getCardapio().enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Extrai a lista de produtos de dentro do objeto de resposta
                    List<Produto> produtos = response.body().getProducts();
                    new InsertAsyncTask(produtoDAO, () -> buscarDoBanco(callback)).execute(produtos);
                } else {
                    // Se a chamada de rede falhar, busca os dados do banco local
                    buscarDoBanco(callback);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                // Em caso de falha de rede, busca os dados do banco local
                buscarDoBanco(callback);
            }
        });
    }

    private void buscarDoBanco(final RepositoryCallback<List<Produto>> callback) {
        new SelectAsyncTask(produtoDAO, callback).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<List<Produto>, Void, Void> {
        private ProdutoDAO asyncTaskDao;
        private Runnable onFinish;

        InsertAsyncTask(ProdutoDAO dao, Runnable onFinish) {
            asyncTaskDao = dao;
            this.onFinish = onFinish;
        }

        @Override
        protected Void doInBackground(final List<Produto>... params) {
            // Garante que a operação no banco de dados seja segura
            if (params.length > 0 && params[0] != null) {
                asyncTaskDao.deleteAll();
                asyncTaskDao.insertAll(params[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (onFinish != null) {
                onFinish.run();
            }
        }
    }

    private static class SelectAsyncTask extends AsyncTask<Void, Void, List<Produto>> {
        private ProdutoDAO asyncTaskDao;
        private RepositoryCallback<List<Produto>> callback;

        SelectAsyncTask(ProdutoDAO dao, RepositoryCallback<List<Produto>> callback) {
            asyncTaskDao = dao;
            this.callback = callback;
        }

        @Override
        protected List<Produto> doInBackground(Void... voids) {
            return asyncTaskDao.getAll();
        }

        @Override
        protected void onPostExecute(List<Produto> produtos) {
            super.onPostExecute(produtos);
            if (callback != null) {
                callback.onComplete(produtos);
            }
        }
    }
}
