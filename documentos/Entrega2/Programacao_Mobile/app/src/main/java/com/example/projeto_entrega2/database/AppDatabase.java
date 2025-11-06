package com.example.projeto_entrega2.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.projeto_entrega2.model.Produto;
import com.example.projeto_entrega2.model.ProdutoDAO;

@Database(entities = {Produto.class}, version = 1, exportSchema = false) // MUDANÇA AQUI
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProdutoDAO produtoDAO();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "comendaria_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
