package ru.nemodev.project.quotes.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;
import ru.nemodev.project.quotes.entity.internal.QuoteInfo;
import ru.nemodev.project.quotes.entity.internal.QuoteInternal;

@Dao
public interface QuoteInternalDAO
{
    @Transaction
    @Query("SELECT * FROM quotes")
    Single<List<QuoteInfo>> getAll();

    @Transaction
    @Query("SELECT * FROM quotes WHERE liked = '1'")
    Single<List<QuoteInfo>> getLiked();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<QuoteInternal> quotes);

    @Update
    void update(QuoteInternal quoteInternal);
}
