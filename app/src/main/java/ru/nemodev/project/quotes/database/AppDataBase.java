package ru.nemodev.project.quotes.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import ru.nemodev.project.quotes.app.QuoteApp;
import ru.nemodev.project.quotes.entity.internal.AuthorInternal;
import ru.nemodev.project.quotes.entity.internal.CategoryInternal;
import ru.nemodev.project.quotes.entity.internal.QuoteInternal;

@Database(entities = {QuoteInternal.class, AuthorInternal.class, CategoryInternal.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase
{
    private static final String DATA_BASE_NAME = "quotes.db";

    private static volatile AppDataBase instance;

    public abstract QuoteInternalDAO quoteDAO();
    public abstract AuthorInternalDAO authorDAO();
    public abstract CategoryInternalDAO categoryDAO();

    public static AppDataBase getInstance()
    {
        if (instance == null)
        {
            synchronized (AppDataBase.class)
            {
                if (instance == null)
                {
                    instance = Room.databaseBuilder(QuoteApp.getInstance(), AppDataBase.class, DATA_BASE_NAME).build();
                }
            }
        }

        return instance;
    }
}