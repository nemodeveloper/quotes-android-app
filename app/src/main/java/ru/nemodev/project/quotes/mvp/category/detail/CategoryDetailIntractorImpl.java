package ru.nemodev.project.quotes.mvp.category.detail;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.database.AppDataBase;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteCacheService;

public class CategoryDetailIntractorImpl implements CategoryDetailContract.CategoryDetailIntractor
{
    @Override
    public void loadQuotes(Long categoryId, OnFinishLoadListener onFinishLoadListener, boolean fromCache)
    {
        if (fromCache)
            loadQuotesFromCache(categoryId, onFinishLoadListener);
        else
            loadQuotesFromGate(categoryId, onFinishLoadListener);
    }

    private void loadQuotesFromGate(Long categoryId, OnFinishLoadListener onFinishLoadListener)
    {
        QuoteCacheService.getInstance().getByCategory(categoryId)
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

    private void loadQuotesFromCache(Long categoryId, OnFinishLoadListener onFinishLoadListener)
    {
        AppDataBase.getInstance().getQuoteDAO().getByCategoryId(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<QuoteInfo>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

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
