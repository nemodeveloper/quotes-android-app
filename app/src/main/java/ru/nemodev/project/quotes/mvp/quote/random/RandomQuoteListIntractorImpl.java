package ru.nemodev.project.quotes.mvp.quote.random;

import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.database.AppDataBase;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteCacheService;

public class RandomQuoteListIntractorImpl implements RandomQuoteListContract.RandomQuoteListIntractor
{
    @Override
    public void loadQuotes(OnFinishLoadListener onFinishLoadListener, Map<String, String> params, boolean fromCache)
    {
        if (fromCache)
            loadFromCache(onFinishLoadListener, params);
        else
            loadFromGate(onFinishLoadListener, params);
    }

    private void loadFromGate(OnFinishLoadListener onFinishLoadListener, Map<String, String> params)
    {
        QuoteCacheService.getInstance().getRandom(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<QuoteInfo>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<QuoteInfo> quoteInfoList)
                    {
                        onFinishLoadListener.onFinishLoad(quoteInfoList);
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

    private void loadFromCache(OnFinishLoadListener onFinishLoadListener, Map<String, String> params)
    {
        AppDataBase.getInstance().getQuoteDAO().getRandom(Integer.parseInt(params.get("count")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<QuoteInfo>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(List<QuoteInfo> quoteInfoList)
                    {
                        onFinishLoadListener.onFinishLoad(quoteInfoList);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onFinishLoadListener.onLoadError(e);
                    }
                });
    }
}
