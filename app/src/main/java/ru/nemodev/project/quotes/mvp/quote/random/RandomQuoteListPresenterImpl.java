package ru.nemodev.project.quotes.mvp.quote.random;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.external.Quote;


public class RandomQuoteListPresenterImpl implements RandomQuoteListContract.RandomQuoteListPresenter,
        RandomQuoteListContract.RandomQuoteListIntractor.OnFinishLoadListener
{
    private final RandomQuoteListContract.RandomQuoteListView view;
    private final RandomQuoteListContract.RandomQuoteListIntractor model;

    private volatile AtomicBoolean isFirstDataLoading = new AtomicBoolean(true);

    public RandomQuoteListPresenterImpl(RandomQuoteListContract.RandomQuoteListView view)
    {
        this.view = view;
        this.model = new RandomQuoteListIntractorImpl();
    }

    @Override
    public void loadNextQuotes()
    {
        if (isFirstDataLoading.get())
        {
            view.showLoader();
            isFirstDataLoading.set(false);
        }

        model.loadQuotes(this);
    }

    @Override
    public void onFinishLoad(List<Quote> quotes)
    {
        view.showNextQuotes(quotes);
        view.hideLoader();
    }

    @Override
    public void onLoadError(Throwable t)
    {
        Observable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Long delay)
                    {
                        loadNextQuotes();
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }
}
