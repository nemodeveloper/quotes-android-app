package ru.nemodev.project.quotes.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import ru.nemodev.project.quotes.app.QuoteApp;
import ru.nemodev.project.quotes.entity.Author;
import ru.nemodev.project.quotes.entity.Category;
import ru.nemodev.project.quotes.entity.Quote;

@Database(entities = {Quote.class, Author.class, Category.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase
{
    private static final String DATA_BASE_NAME = "quotes.db";

    private static volatile AppDataBase instance;

    public abstract QuoteDAO quoteDAO();
    public abstract AuthorDAO authorDAO();
    public abstract CategoryDAO categoryDAO();

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