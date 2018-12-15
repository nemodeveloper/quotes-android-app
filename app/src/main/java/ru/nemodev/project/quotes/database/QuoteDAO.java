package ru.nemodev.project.quotes.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;
import ru.nemodev.project.quotes.entity.Quote;
import ru.nemodev.project.quotes.entity.QuoteInfo;

@Dao
public interface QuoteDAO
{
    @Transaction
    @Query("SELECT * FROM quotes " +
            "INNER JOIN authors ON authors.id = quotes.author_id " +
            "INNER JOIN categories ON categories.id = quotes.category_id")
    Single<List<QuoteInfo>> getAll();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<Quote> quotes);

    @Update
    void update(List<Quote> quotes);
}
