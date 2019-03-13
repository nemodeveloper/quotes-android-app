package ru.nemodev.project.quotes.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

public final class DataBaseMigration
{
    public static final Migration MIGRATION_1_2 = new Migration(1, 2)
    {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database)
        {
            database.execSQL("ALTER TABLE quotes ADD COLUMN liked INTEGER NOT NULL DEFAULT 0");
        }
    };

}
