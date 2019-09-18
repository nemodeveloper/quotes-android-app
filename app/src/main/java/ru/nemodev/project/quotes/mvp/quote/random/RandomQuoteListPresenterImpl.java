package ru.nemodev.project.quotes.mvp.quote.random;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.mvp.quote.QuoteInteractor;
import ru.nemodev.project.quotes.mvp.quote.QuoteInteractorImpl;


public class RandomQuoteListPresenterImpl implements
        RandomQuoteListContract.RandomQuoteListPresenter,
        QuoteInteractor.OnFinishLoadListener
{
    private static final Map<String, String> randomLoadParams = Collections.singletonMap("count", "200");

    private final RandomQuoteListContract.RandomQuoteListView view;
    private final QuoteInteractor quoteInteractor;

    private volatile AtomicBoolean isFirstDataLoading = new AtomicBoolean(true);

    public RandomQuoteListPresenterImpl(RandomQuoteListContract.RandomQuoteListView view)
    {
        this.view = view;
        this.quoteInteractor = new QuoteInteractorImpl();
    }

    @Override
    public void loadNextQuotes()
    {
        if (isFirstDataLoading.get())
        {
            view.showLoader();
            isFirstDataLoading.set(false);
        }

        quoteInteractor.loadRandom(this, randomLoadParams, false);
    }

    @Override
    public void onFinishLoad(List<QuoteInfo> quotes, boolean fromCache)
    {
        view.showNextQuotes(quotes);
        view.hideLoader();
    }

    @Override
    public void onLoadError(Throwable t, boolean fromCache)
    {
        quoteInteractor.loadRandom(this, randomLoadParams, true);
    }
}
