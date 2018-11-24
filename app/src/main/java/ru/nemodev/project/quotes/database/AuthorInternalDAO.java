package ru.nemodev.project.quotes.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import ru.nemodev.project.quotes.entity.internal.AuthorInternal;

@Dao
public interface AuthorInternalDAO
{
    @Query("SELECT * FROM authors")
    Single<List<AuthorInternal>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<AuthorInternal> authorInternals);
}
