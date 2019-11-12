package ru.nemodev.project.quotes.ui.quote;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;
import ru.nemodev.project.quotes.service.quote.QuoteService;


public class QuoteInteractorImpl implements QuoteInteractor
{
    @Override
    public Observable<List<QuoteInfo>> loadRandom(Integer count)
    {
        return QuoteService.getInstance().getRandom(count)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<QuoteInfo>> loadByAuthor(Long authorId)
    {
        return QuoteService.getInstance().getByAuthor(authorId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<QuoteInfo>> loadByCategory(Long categoryId)
    {
        return QuoteService.getInstance().getByCategory(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<QuoteInfo> getById(Long quoteId)
    {
        return AppDataBase.getInstance().getQuoteRepository().getById(quoteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
