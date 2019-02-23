package ru.nemodev.project.quotes.mvp.quote.random;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nemodev.project.quotes.entity.QuoteInfo;


public class RandomQuoteListPresenterImpl implements RandomQuoteListContract.RandomQuoteListPresenter,
        RandomQuoteListContract.RandomQuoteListIntractor.OnFinishLoadListener
{
    private static final Map<String, String> randomLoadParams = Collections.singletonMap("count", "200");

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

        model.loadQuotes(this, randomLoadParams, false);
    }

    @Override
    public void onFinishLoad(List<QuoteInfo> quotes)
    {
        view.showNextQuotes(quotes);
        view.hideLoader();
    }

    @Override
    public void onLoadError(Throwable t)
    {
        model.loadQuotes(this, randomLoadParams, true);
    }
}
