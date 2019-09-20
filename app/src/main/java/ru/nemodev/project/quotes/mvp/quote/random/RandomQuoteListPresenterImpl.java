package ru.nemodev.project.quotes.mvp.quote.random;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.mvp.quote.QuoteInteractor;
import ru.nemodev.project.quotes.mvp.quote.QuoteInteractorImpl;


public class RandomQuoteListPresenterImpl implements
        RandomQuoteListContract.RandomQuoteListPresenter,
        QuoteInteractor.OnFinishLoadListener
{
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

        quoteInteractor.loadRandom(this, 200);
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
        view.hideLoader();
    }
}
