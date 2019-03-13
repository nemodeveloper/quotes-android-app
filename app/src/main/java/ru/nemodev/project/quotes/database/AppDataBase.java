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

    public abstract QuoteDAO getQuoteDAO();
    public abstract AuthorDAO getAuthorDAO();
    public abstract CategoryDAO getCategoryDAO();

    public static AppDataBase getInstance()
    {
        if (instance == null)
        {
            synchronized (AppDataBase.class)
            {
                if (instance == null)
                {
                    instance = Room.databaseBuilder(QuoteApp.getInstance(), AppDataBase.class, DATA_BASE_NAME)
                            .addMigrations(DataBaseMigration.MIGRATION_1_2)
                            .build();
                }
            }
        }

        return instance;
    }
}