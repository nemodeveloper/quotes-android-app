package ru.nemodev.project.quotes.repository.cache.quote;

import androidx.collection.LruCache;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;


public class QuoteCacheRepositoryImpl implements QuoteCacheRepository {

    private static final String GET_BY_AUTHOR_PREF_KEY = "getByAuthor_";
    private static final String GET_BY_CATEGORY_PREF_KEY = "getByCategory_";

    private final LruCache<String, List<QuoteInfo>> quoteCache;

    public QuoteCacheRepositoryImpl() {
        quoteCache = new LruCache<>(2);
    }

    @Override
    public Observable<List<QuoteInfo>> getByAuthor(Long authorId) {
        List<QuoteInfo> quoteInfoList = quoteCache.get(GET_BY_AUTHOR_PREF_KEY + authorId);
        if (quoteInfoList == null)
            quoteInfoList = Collections.emptyList();

        return Observable.just(quoteInfoList)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void putAuthorQuotes(Long authorId, List<QuoteInfo> quoteInfoList) {
        quoteCache.put(GET_BY_AUTHOR_PREF_KEY + authorId, quoteInfoList);
    }

    @Override
    public Observable<List<QuoteInfo>> getByCategory(Long categoryId) {
        List<QuoteInfo> quoteInfoList = quoteCache.get(GET_BY_CATEGORY_PREF_KEY + categoryId);
        if (quoteInfoList == null)
            quoteInfoList = Collections.emptyList();

        return Observable.just(quoteInfoList)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void putCategoryQuotes(Long categoryId, List<QuoteInfo> quoteInfoList) {
        quoteCache.put(GET_BY_CATEGORY_PREF_KEY + categoryId, quoteInfoList);
    }
}
