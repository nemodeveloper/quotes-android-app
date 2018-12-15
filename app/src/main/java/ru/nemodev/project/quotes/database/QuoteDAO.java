package ru.nemodev.project.quotes.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.reactivex.Single;
import ru.nemodev.project.quotes.entity.Quote;
import ru.nemodev.project.quotes.entity.QuoteInfo;

@Dao
public interface QuoteDAO
{
    @Transaction
    @Query("SELECT * FROM quotes")
    Single<List<QuoteInfo>> getAll();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<Quote> quotes);

    @Transaction
    @Query("SELECT * FROM quotes" +
            " WHERE quotes.author_id = :authorId")
    Single<List<QuoteInfo>> getByAuthor(long authorId);

    @Transaction
    @Query("SELECT * FROM quotes" +
            " WHERE quotes.category_id = :categoryId")
    Single<List<QuoteInfo>> getByCategoryId(long categoryId);
}
