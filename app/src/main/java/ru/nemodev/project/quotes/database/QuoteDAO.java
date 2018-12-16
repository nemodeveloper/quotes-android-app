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
import ru.nemodev.project.quotes.utils.AuthorUtils;
import ru.nemodev.project.quotes.utils.CategoryUtils;
import ru.nemodev.project.quotes.utils.QuoteUtils;

@Dao
public abstract class QuoteDAO
{
    @Transaction
    @Query("SELECT * FROM quotes ORDER BY RANDOM() LIMIT 200")
    public abstract Single<List<QuoteInfo>> getRandom();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void add(List<Quote> quotes);

    @Transaction
    @Query("SELECT * FROM quotes" +
            " WHERE quotes.author_id = :authorId")
    public abstract Single<List<QuoteInfo>> getByAuthor(long authorId);

    @Transaction
    @Query("SELECT * FROM quotes" +
            " WHERE quotes.category_id = :categoryId")
    public abstract Single<List<QuoteInfo>> getByCategoryId(long categoryId);

    @Transaction
    public void addQuoteInfo(List<QuoteInfo> quoteInfoList)
    {
        add(QuoteUtils.fromQuotesInfo(quoteInfoList));
        AppDataBase.getInstance().getCategoryDAO().add(CategoryUtils.getCategories(quoteInfoList));
        AppDataBase.getInstance().getAuthorDAO().add(AuthorUtils.getAuthors(quoteInfoList));
    }
}
