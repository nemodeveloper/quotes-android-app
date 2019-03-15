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
    @Query("SELECT * FROM quotes ORDER BY RANDOM() LIMIT :count")
    public abstract Single<List<QuoteInfo>> getRandom(int count);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void add(List<Quote> quotes);

    @Query("SELECT * FROM quotes" +
            " WHERE quotes.author_id = :authorId")
    public abstract Single<List<QuoteInfo>> getByAuthor(long authorId);

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
    @Query("UPDATE quotes SET liked = :liked WHERE id = :id")
    public abstract void like(Long id, boolean liked);

    public Observable<Quote> like(Quote quote)
    {
        return Observable.create(emitter ->
        {
            like(quote.getId(), quote.getLiked());

            emitter.onNext(quote);
            emitter.onComplete();
        });
    }

    @Query("SELECT id FROM quotes WHERE id IN (:quotesForCheck) AND liked = 1")
    public abstract List<Long> getLiked(List<Long> quotesForCheck);

    @Query("SELECT * FROM quotes WHERE liked = 1")
    public abstract Single<List<QuoteInfo>> getLiked();
}
