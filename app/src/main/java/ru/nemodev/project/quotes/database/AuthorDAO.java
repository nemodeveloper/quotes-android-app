package ru.nemodev.project.quotes.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.reactivex.Single;
import ru.nemodev.project.quotes.entity.Author;

@Dao
public interface AuthorDAO
{
    @Transaction
    @Query("SELECT * FROM authors ORDER BY full_name")
    Single<List<Author>> getAll();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<Author> authors);
}
