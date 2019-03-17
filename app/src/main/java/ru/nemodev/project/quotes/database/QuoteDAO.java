package ru.nemodev.project.quotes.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.reactivex.Observable;
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
    @Query("SELECT * FROM quotes ORDER BY RANDOM() LIMIT :count")
    public abstract Single<List<QuoteInfo>> getRandom(int count);

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
        AppDataBase.getInstance().getCategoryDAO().add(CategoryUtils.getCategories(quoteInfoList));
        AppDataBase.getInstance().getAuthorDAO().add(AuthorUtils.getAuthors(quoteInfoList));
        add(QuoteUtils.fromQuotesInfo(quoteInfoList));
    }

    @Transaction
    @Query("UPDATE quotes SET liked = :liked, like_date = :likeDate WHERE id = :id")
    public abstract void like(Long id, boolean liked, Long likeDate);

    public Observable<Quote> likeAsync(Quote quote)
    {
        return Observable.fromCallable(() -> {
            like(quote.getId(), quote.getLiked(), DataTypeConverter.fromCalendar(quote.getLikeDate()));
            return quote;
        });
    }

    @Transaction
    @Query("SELECT id FROM quotes WHERE id IN (:quotesForCheck) AND liked = 1")
    public abstract List<Long> getLiked(List<Long> quotesForCheck);

    @Transaction
    @Query("SELECT * FROM quotes WHERE liked = 1 ORDER BY like_date DESC")
    public abstract Single<List<QuoteInfo>> getLiked();
}
