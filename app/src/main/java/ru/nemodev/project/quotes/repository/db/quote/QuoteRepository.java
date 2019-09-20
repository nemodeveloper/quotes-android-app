package ru.nemodev.project.quotes.repository.db.quote;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nemodev.project.quotes.entity.author.AuthorUtils;
import ru.nemodev.project.quotes.entity.category.CategoryUtils;
import ru.nemodev.project.quotes.entity.quote.Quote;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.entity.quote.QuoteUtils;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;
import ru.nemodev.project.quotes.repository.db.room.DataTypeConverter;

@Dao
public abstract class QuoteRepository
{
    @Transaction
    @Query("SELECT * FROM quotes ORDER BY RANDOM() LIMIT :count")
    public abstract Single<List<QuoteInfo>> getRandom(int count);

    @Transaction
    @Query("SELECT * FROM quotes WHERE id = :id")
    public abstract Single<QuoteInfo> getById(Long id);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void add(List<Quote> quotes);

    @Transaction
    @Query("SELECT * FROM quotes" +
            " WHERE quotes.author_id = :authorId")
    public abstract Single<List<QuoteInfo>> getByAuthorId(Long authorId);

    @Transaction
    @Query("SELECT * FROM quotes" +
            " WHERE quotes.category_id = :categoryId")
    public abstract Single<List<QuoteInfo>> getByCategoryId(Long categoryId);

    @Transaction
    public void addQuoteInfo(List<QuoteInfo> quoteInfoList)
    {
        AppDataBase.getInstance().getCategoryRepository().add(CategoryUtils.getCategories(quoteInfoList));
        AppDataBase.getInstance().getAuthorRepository().add(AuthorUtils.getAuthors(quoteInfoList));
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
