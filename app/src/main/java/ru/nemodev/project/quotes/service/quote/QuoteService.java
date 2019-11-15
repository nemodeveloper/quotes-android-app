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
import ru.nemodev.project.quotes.repository.db.quote.QuoteRepository;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;


public class QuoteService {
    private static volatile QuoteService instance = new QuoteService();

    private final QuoteRepository quoteRepository;
    private final QuoteApi quoteApi;

    private QuoteService() {
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
                quoteRepository.getRandom(count))
                    .filter(CollectionUtils::isNotEmpty)
                    .first(Collections.emptyList())
                    .toObservable()
                    .subscribeOn(Schedulers.io());
    }

    public Observable<QuoteInfo> getById(Long quoteId)
    {
        return AppDataBase.getInstance().getQuoteRepository().getById(quoteId)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> syncWithServerByAuthor(Long authorId) {
        return quoteApi.getByAuthor(authorId)
                .map(this::saveToDataBase)
                .flatMap(quoteInfos -> Observable.just(true))
                .onErrorResumeNext(Observable.just(false))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> syncWithServerByCategory(Long categoryId) {
        return quoteApi.getByCategory(categoryId)
                .map(this::saveToDataBase)
                .flatMap(quoteInfos -> Observable.just(true))
                .onErrorResumeNext(Observable.just(false))
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