package com.example.projeto_entrega2.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.projeto_entrega2.model.CartItem;
import com.example.projeto_entrega2.model.CartItemDAO;
import com.example.projeto_entrega2.model.Produto;
import com.example.projeto_entrega2.model.ProdutoDAO;
import com.example.projeto_entrega2.model.User;
import com.example.projeto_entrega2.model.UserDAO;

@Database(entities = {Produto.class, User.class, CartItem.class}, version = 4, exportSchema = false) // VERSÃO ATUALIZADA
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProdutoDAO produtoDAO();
    public abstract UserDAO userDAO();
    public abstract CartItemDAO cartItemDAO(); // NOVO DAO

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "comedoria_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
