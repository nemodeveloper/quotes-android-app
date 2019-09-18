package ru.nemodev.project.quotes.repository.database;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.nemodev.core.app.AndroidApplication;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.entity.category.Category;
import ru.nemodev.project.quotes.entity.quote.Quote;
import ru.nemodev.project.quotes.repository.author.AuthorRepository;
import ru.nemodev.project.quotes.repository.category.CategoryRepository;
import ru.nemodev.project.quotes.repository.quote.QuoteRepository;

@Database(entities = {Quote.class, Author.class, Category.class}, version = 5)
public abstract class AppDataBase extends RoomDatabase
{
    private static final String DATA_BASE_NAME = "quotes.db";

    private static volatile AppDataBase instance;

    public abstract QuoteRepository getQuoteDAO();
    public abstract AuthorRepository getAuthorDAO();
    public abstract CategoryRepository getCategoryDAO();

    public static AppDataBase getInstance()
    {
        if (instance == null)
        {
            synchronized (AppDataBase.class)
            {
                if (instance == null)
                {
                    instance = Room.databaseBuilder(AndroidApplication.getInstance(), AppDataBase.class, DATA_BASE_NAME)
                            .addMigrations(
                                    DataBaseMigration.MIGRATION_1_2,
                                    DataBaseMigration.MIGRATION_2_3,
                                    DataBaseMigration.MIGRATION_3_4,
                                    DataBaseMigration.MIGRATION_4_5)
                            .build();
                }
            }
        }

        return instance;
    }
}