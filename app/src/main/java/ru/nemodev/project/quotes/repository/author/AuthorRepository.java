package ru.nemodev.project.quotes.repository.author;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.Single;
import ru.nemodev.project.quotes.entity.author.Author;

@Dao
public interface AuthorRepository
{
    @Transaction
    @Query("SELECT * FROM authors ORDER BY full_name")
    Single<List<Author>> getAll();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<Author> authors);
}
