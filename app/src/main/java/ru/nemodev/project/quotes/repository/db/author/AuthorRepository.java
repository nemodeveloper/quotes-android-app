package ru.nemodev.project.quotes.repository.db.author;


import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.Observable;
import ru.nemodev.project.quotes.entity.author.Author;

@Dao
public interface AuthorRepository
{
    @Transaction
    @Query("SELECT * FROM authors ORDER BY full_name COLLATE UNICODE")
    Observable<List<Author>> getAll();

    @Transaction
    @Query("SELECT * FROM authors ORDER BY full_name COLLATE UNICODE")
    DataSource.Factory<Integer, Author> getAllLiveData();

    @Transaction
    @Query("SELECT * FROM authors a WHERE upper(a.full_name) like '%' || upper(:name) || '%' ORDER BY a.full_name COLLATE UNICODE")
    DataSource.Factory<Integer, Author> findByNameLiveData(String name);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<Author> authors);
}
