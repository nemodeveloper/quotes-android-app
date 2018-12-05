package ru.nemodev.project.quotes.mvp.quote.random;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.external.Quote;
import ru.nemodev.project.quotes.service.quote.QuoteCacheService;

public class RandomQuoteListIntractorImpl implements RandomQuoteListContract.RandomQuoteListIntractor
{
    @Override
    public void loadQuotes(OnFinishLoadListener onFinishLoadListener)
    {
        final Observer<List<Quote>> loadNewItemsSubscriber = new Observer<List<Quote>>()
        {
            @Override
            public void onError(Throwable e)
            {
                onFinishLoadListener.onLoadError(e);
            }

            @Override
            public void onComplete() { }

            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(List<Quote> items)
            {
                onFinishLoadListener.onFinishLoad(items);
            }
        };

        QuoteCacheService.getInstance().getRandom(Collections.singletonMap("count", "200"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loadNewItemsSubscriber);
    }
}
