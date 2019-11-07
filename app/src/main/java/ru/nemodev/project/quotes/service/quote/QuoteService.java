package ru.nemodev.project.quotes.service.quote;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.entity.quote.QuoteUtils;
import ru.nemodev.project.quotes.repository.api.dto.QuoteDTO;
import ru.nemodev.project.quotes.repository.api.quote.QuoteApi;
import ru.nemodev.project.quotes.repository.api.quote.QuoteApiFactory;
import ru.nemodev.project.quotes.repository.cache.quote.QuoteCacheRepository;
import ru.nemodev.project.quotes.repository.cache.quote.QuoteCacheRepositoryImpl;
import ru.nemodev.project.quotes.repository.db.quote.QuoteRepository;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;


public class QuoteService {
    private static volatile QuoteService instance = new QuoteService();

    private final QuoteCacheRepository quoteCacheRepository;
    private final QuoteRepository quoteRepository;
    private final QuoteApi quoteApi;

    private QuoteService() {
        quoteCacheRepository = new QuoteCacheRepositoryImpl();
        quoteRepository = AppDataBase.getInstance().getQuoteRepository();
        quoteApi = new QuoteApiFactory().createApi();
    }

    public static QuoteService getInstance() {
        return instance;
    }

    public Observable<List<QuoteInfo>> getRandom(Integer count) {
        Observable<List<QuoteInfo>> gatewayObservable = quoteApi
                .getRandom(Collections.singletonMap("count", count.toString()))
                .map(this::saveToDataBase)
                .onErrorResumeNext(Observable.empty());

        return Observable.concat(
                gatewayObservable,
                quoteRepository.getRandom(count).toObservable())
                    .filter(CollectionUtils::isNotEmpty)
                    .first(Collections.emptyList())
                    .toObservable()
                    .subscribeOn(Schedulers.io());
    }

    public Observable<List<QuoteInfo>> getByAuthor(Long authorId) {
        Observable<List<QuoteInfo>> gatewayObservable = quoteApi
                .getByAuthor(authorId)
                .map(this::saveToDataBase)
                .map(quoteInfoList -> {
                    quoteCacheRepository.putAuthorQuotes(authorId, quoteInfoList);
                    return quoteInfoList;
                })
                .onErrorResumeNext(Observable.empty());

        return Observable.concat(
                quoteCacheRepository.getByAuthor(authorId),
                gatewayObservable,
                quoteRepository.getByAuthorId(authorId)
                        .filter(CollectionUtils::isNotEmpty)
                        .map(quoteInfoList -> {
                            quoteCacheRepository.putAuthorQuotes(authorId, quoteInfoList);
                            return quoteInfoList;
                        })
                        .toObservable()
                )
                    .filter(CollectionUtils::isNotEmpty)
                    .first(Collections.emptyList())
                    .toObservable()
                    .subscribeOn(Schedulers.io());
    }

    public Observable<List<QuoteInfo>> getByCategory(Long categoryId) {
        Observable<List<QuoteInfo>> gatewayObservable = quoteApi
                .getByCategory(categoryId)
                .map(this::saveToDataBase)
                .map(quoteInfoList -> {
                    quoteCacheRepository.putCategoryQuotes(categoryId, quoteInfoList);
                    return quoteInfoList;
                })
                .onErrorResumeNext(Observable.empty());

        return Observable.concat(
                quoteCacheRepository.getByCategory(categoryId),
                gatewayObservable,
                quoteRepository.getByCategoryId(categoryId)
                        .filter(CollectionUtils::isNotEmpty)
                        .map(quoteInfoList -> {
                            quoteCacheRepository.putCategoryQuotes(categoryId, quoteInfoList);
                            return quoteInfoList;
                        })
                        .toObservable()
                )
                    .filter(CollectionUtils::isNotEmpty)
                    .first(Collections.emptyList())
                    .toObservable()
                    .subscribeOn(Schedulers.io());
    }

    private List<QuoteInfo> saveToDataBase(List<QuoteDTO> quotes) {
        // Необходимо т.к запрос к БД не держит много переменных в IN ()
        List<List<Long>> quoteIds = ListUtils.partition(QuoteUtils.getQuoteIds(quotes), 100);
        Set<Long> likedQuoteIds = new HashSet<>();
        for (List<Long> list : quoteIds)
        {
           likedQuoteIds.addAll(AppDataBase.getInstance().getQuoteRepository().getLiked(list));
        }

        List<QuoteInfo> quoteInfos = QuoteUtils.toQuotesInfo(quotes, likedQuoteIds);
        AppDataBase.getInstance().getQuoteRepository().addQuoteInfo(quoteInfos);

        return quoteInfos;
    }
}