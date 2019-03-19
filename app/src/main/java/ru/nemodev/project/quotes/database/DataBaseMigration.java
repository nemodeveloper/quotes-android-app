package ru.nemodev.project.quotes.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

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
        }
    };

}
