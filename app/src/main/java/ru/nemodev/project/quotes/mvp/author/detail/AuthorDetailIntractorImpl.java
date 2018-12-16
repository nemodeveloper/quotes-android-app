package ru.nemodev.project.quotes.mvp.author.detail;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.database.AppDataBase;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteCacheService;

public class AuthorDetailIntractorImpl implements AuthorDetailContract.AuthorDetailIntractor
{
    @Override
    public void loadQuotes(Long authorId, OnFinishLoadListener onFinishLoadListener, boolean fromCache)
    {
        if (fromCache)
            loadQuotesFromCache(authorId, onFinishLoadListener);
        else
            loadQuotesFromGate(authorId, onFinishLoadListener);
    }

    private void loadQuotesFromGate(Long authorId, OnFinishLoadListener onFinishLoadListener)
    {
        QuoteCacheService.getInstance().getByAuthor(authorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<QuoteInfo>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<QuoteInfo> quoteInfoList)
                    {
                        onFinishLoadListener.onFinishLoad(quoteInfoList, false);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onFinishLoadListener.onLoadError(e);
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    private void loadQuotesFromCache(Long authorId, OnFinishLoadListener onFinishLoadListener)
    {
        AppDataBase.getInstance().getQuoteDAO().getByAuthor(authorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<QuoteInfo>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(List<QuoteInfo> quoteInfoList)
                    {
                        onFinishLoadListener.onFinishLoad(quoteInfoList, true);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onFinishLoadListener.onLoadError(e);
                    }
                });
    }
}
