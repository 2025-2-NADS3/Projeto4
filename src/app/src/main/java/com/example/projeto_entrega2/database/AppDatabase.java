package com.example.projeto_entrega2.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.projeto_entrega2.model.CartItem;
import com.example.projeto_entrega2.model.CartItemDAO;
import com.example.projeto_entrega2.model.Order;
import com.example.projeto_entrega2.model.OrderDAO;
import com.example.projeto_entrega2.model.Produto;
import com.example.projeto_entrega2.model.ProdutoDAO;
import com.example.projeto_entrega2.model.Usuario;
import com.example.projeto_entrega2.model.UsuarioDAO;

// CORREÇÃO: Versão do banco incrementada para 3 para adicionar a nova coluna pickupCode
@Database(entities = {Produto.class, Usuario.class, CartItem.class, Order.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract ProdutoDAO produtoDAO();
    public abstract UsuarioDAO usuarioDAO();
    public abstract CartItemDAO cartItemDAO();
    public abstract OrderDAO orderDAO();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "compras-db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
