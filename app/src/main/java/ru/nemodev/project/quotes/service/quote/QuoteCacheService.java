package ru.nemodev.project.quotes.service.quote;

import androidx.collection.LruCache;

import org.apache.commons.collections4.ListUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.api.RetrofitAPIFactory;
import ru.nemodev.project.quotes.api.dto.QuoteDTO;
import ru.nemodev.project.quotes.database.AppDataBase;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.entity.QuoteUtils;
import ru.nemodev.project.quotes.utils.LogUtils;

public class QuoteCacheService
{
    private static final String LOG_TAG = QuoteCacheService.class.getSimpleName();

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
        return RetrofitAPIFactory.getQuoteAPI().getRandom(queryParams)
                .map(this::saveToDataBase)
                .subscribeOn(Schedulers.io());
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
                        .map(this::saveToDataBase)
                        .subscribeOn(Schedulers.io());

                observable.subscribe(new Observer<List<QuoteInfo>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<QuoteInfo> quotes)
                    {
                        quoteCache.put(byAuthorKey, quotes);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        LogUtils.logWithReport(LOG_TAG, "Ошибка сохранения цитат по автору в кеш!", e);
                    }

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
                        .map(this::saveToDataBase)
                        .subscribeOn(Schedulers.io());

                observable.subscribe(new Observer<List<QuoteInfo>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<QuoteInfo> quotes)
                    {
                        quoteCache.put(byCategoryKey, quotes);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        LogUtils.logWithReport(LOG_TAG, "Ошибка сохранения цитат по категории в кеш!", e);
                    }

                    @Override
                    public void onComplete() { }
                });

                return observable;
            }

            return Observable.just(quotesByCategory);
        }
    }

    private List<QuoteInfo> saveToDataBase(List<QuoteDTO> quotes)
    {
        // Необходимо т.к запрос к БД не держит много переменных в IN ()
        List<List<Long>> quoteIds = ListUtils.partition(QuoteUtils.getQuoteIds(quotes), 100);
        Set<Long> likedQuoteIds = new HashSet<>();
        for (List<Long> list : quoteIds)
        {
           likedQuoteIds.addAll(AppDataBase.getInstance().getQuoteDAO().getLiked(list));
        }

        List<QuoteInfo> quoteInfos = QuoteUtils.toQuotesInfo(quotes, likedQuoteIds);
        AppDataBase.getInstance().getQuoteDAO().addQuoteInfo(quoteInfos);

        return quoteInfos;
    }
}