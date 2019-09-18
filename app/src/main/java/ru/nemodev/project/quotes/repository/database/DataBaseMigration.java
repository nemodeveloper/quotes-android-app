package ru.nemodev.project.quotes.repository.database;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;

public final class DataBaseMigration
{
    public static final Migration MIGRATION_1_2 = new Migration(1, 2)
    {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database)
        {
            database.execSQL("DROP TABLE quotes");

            database.execSQL("CREATE TABLE IF NOT EXISTS quotes (" +
                    "id INTEGER, " +
                    "category_id INTEGER NOT NULL, " +
                    "text TEXT NOT NULL, " +
                    "author_id INTEGER NOT NULL, " +
                    "source TEXT, " +
                    "`year` TEXT, " +
                    "`liked` INTEGER NOT NULL DEFAULT 0, " +
                    "PRIMARY KEY(`id`))");

            database.execSQL("CREATE INDEX index_quotes_liked ON quotes (liked)");
            database.execSQL("CREATE INDEX index_quotes_category_id ON quotes (category_id)");
            database.execSQL("CREATE INDEX index_quotes_author_id ON quotes (author_id)");
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3)
    {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database)
        {
            database.execSQL("ALTER TABLE quotes ADD COLUMN like_date INTEGER");
            database.execSQL("UPDATE quotes SET like_date = ?",
                    new Object[] {
                            Calendar.getInstance().getTimeInMillis()
                    }
            );
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4)
    {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database)
        {
            database.execSQL("ALTER TABLE authors ADD COLUMN image_url TEXT");
            database.execSQL("ALTER TABLE categories ADD COLUMN image_url TEXT");
        }
    };

    public static final Migration MIGRATION_4_5 = new Migration(4, 5)
    {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database)
        {
            database.execSQL("DELETE FROM categories " +
                    "WHERE id IN (SELECT id FROM categories WHERE id NOT IN (SELECT DISTINCT category_id from quotes))");
        }
    };

}
