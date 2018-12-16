package ru.nemodev.project.quotes.service.quote;

import android.support.v4.util.LruCache;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.api.RetrofitAPIFactory;
import ru.nemodev.project.quotes.database.AppDataBase;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.utils.QuoteUtils;

public class QuoteCacheService
{
    private static volatile QuoteCacheService instance;

    private static final String GET_BY_AUTHOR_PREF_KEY = "getByAuthor_";
    private static final String GET_BY_CATEGORY_PREF_KEY = "getByCategory_";

    private final LruCache<String, List<QuoteInfo>> quoteCache;

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

    public Observable<List<QuoteInfo>> getRandom(Map<String, String> queryParams)
    {
        Observable<List<QuoteInfo>> observable = RetrofitAPIFactory.getQuoteAPI().getRandom(queryParams)
                .map(QuoteUtils::toQuotesInfo)
                .subscribeOn(Schedulers.io());

        observable.subscribe(new Observer<List<QuoteInfo>>()
        {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(List<QuoteInfo> quoteInfoList)
            {
                saveToDataBase(quoteInfoList);
            }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }
        });

        return observable;
    }

    public Observable<List<QuoteInfo>> getByAuthor(Long authorId)
    {
        synchronized (QuoteCacheService.class)
        {
            final String byAuthorKey = GET_BY_AUTHOR_PREF_KEY + authorId;

            List<QuoteInfo> quotesByAuthor = quoteCache.get(byAuthorKey);
            if (quotesByAuthor == null)
            {
                Observable<List<QuoteInfo>> observable = RetrofitAPIFactory.getQuoteAPI().getByAuthor(authorId)
                        .map(QuoteUtils::toQuotesInfo)
                        .subscribeOn(Schedulers.io());

                observable.subscribe(new Observer<List<QuoteInfo>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<QuoteInfo> quotes)
                    {
                        quoteCache.put(byAuthorKey, quotes);
                        saveToDataBase(quotes);
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

    public Observable<List<QuoteInfo>> getByCategory(Long categoryId)
    {
        synchronized (QuoteCacheService.class)
        {
            final String byCategoryKey = GET_BY_CATEGORY_PREF_KEY + categoryId;

            List<QuoteInfo> quotesByCategory = quoteCache.get(byCategoryKey);
            if (quotesByCategory == null)
            {
                Observable<List<QuoteInfo>> observable = RetrofitAPIFactory.getQuoteAPI().getByCategory(categoryId)
                        .map(QuoteUtils::toQuotesInfo)
                        .subscribeOn(Schedulers.io());

                observable.subscribe(new Observer<List<QuoteInfo>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<QuoteInfo> quotes)
                    {
                        quoteCache.put(byCategoryKey, quotes);
                        saveToDataBase(quotes);
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

    private void saveToDataBase(List<QuoteInfo> quoteInfoList)
    {
        Observable.just(quoteInfoList)
                .observeOn(Schedulers.io())
                .subscribe(AppDataBase.getInstance().getQuoteDAO()::addQuoteInfo);
    }
}