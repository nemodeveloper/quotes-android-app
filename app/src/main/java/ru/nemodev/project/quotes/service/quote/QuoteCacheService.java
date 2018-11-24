package ru.nemodev.project.quotes.service.quote;

import android.support.v4.util.LruCache;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.external.Quote;
import ru.nemodev.project.quotes.service.RetrofitServiceFactory;

public class QuoteCacheService
{
    private static volatile QuoteCacheService instance;

    private static final String GET_BY_AUTHOR_PREF_KEY = "getByAuthor_";
    private static final String GET_BY_CATEGORY_PREF_KEY = "getByCategory_";

    private final LruCache<String, List<Quote>> quoteCache;

    private QuoteCacheService(int maxSize)
    {
        quoteCache = new LruCache<>(maxSize);
    }

    public static QuoteCacheService getInstance()
    {
        if (instance == null)
        {
            synchronized (QuoteCacheService.class)
            {
                if (instance == null)
                {
                    instance = new QuoteCacheService(3);
                }
            }
        }

        return instance;
    }

    public Observable<List<Quote>> getRandom(Map<String, String> queryParams)
    {
        return RetrofitServiceFactory.getQuoteService().getRandom(queryParams);
    }

    public Observable<List<Quote>> getByAuthor(Long authorId)
    {
        synchronized (QuoteCacheService.class)
        {
            final String byAuthorKey = GET_BY_AUTHOR_PREF_KEY + authorId;

            List<Quote> quotesByAuthor = quoteCache.get(byAuthorKey);
            if (quotesByAuthor == null)
            {
                Observable<List<Quote>> observable = RetrofitServiceFactory.getQuoteService().getByAuthor(authorId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

                observable.subscribe(new Observer<List<Quote>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<Quote> quotes)
                    {
                        quoteCache.put(byAuthorKey, quotes);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });

                return observable;
            }

            return Observable.just(quotesByAuthor);
        }
    }

    public Observable<List<Quote>> getByCategory(Long categoryId)
    {
        synchronized (QuoteCacheService.class)
        {
            final String byCategoryKey = GET_BY_CATEGORY_PREF_KEY + categoryId;

            List<Quote> quotesByCategory = quoteCache.get(byCategoryKey);
            if (quotesByCategory == null)
            {
                Observable<List<Quote>> observable = RetrofitServiceFactory.getQuoteService().getByCategory(categoryId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

                observable.subscribe(new Observer<List<Quote>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<Quote> quotes)
                    {
                        quoteCache.put(byCategoryKey, quotes);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });

                return observable;
            }

            return Observable.just(quotesByCategory);
        }
    }

    public Observable<List<Quote>> getLiked()
    {
        return Observable.just(Collections.emptyList());
    }
}