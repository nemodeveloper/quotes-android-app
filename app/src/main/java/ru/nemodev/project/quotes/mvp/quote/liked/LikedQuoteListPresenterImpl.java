package ru.nemodev.project.quotes.mvp.quote.liked;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.mvp.quote.QuoteInteractor;
import ru.nemodev.project.quotes.mvp.quote.QuoteInteractorImpl;


public class LikedQuoteListPresenterImpl implements
        LikedQuoteListContract.LikedQuoteListPresenter,
        QuoteInteractor.OnFinishLoadListener
{
    private final LikedQuoteListContract.LikedQuoteListView view;
    private final QuoteInteractor quoteInteractor;

    private volatile AtomicBoolean isDataLoading = new AtomicBoolean(false);

    public LikedQuoteListPresenterImpl(LikedQuoteListContract.LikedQuoteListView view)
    {
        this.view = view;
        this.quoteInteractor = new QuoteInteractorImpl();
    }

    @Override
    public void loadLikedQuotes()
    {
        if (isDataLoading.get())
            return;

        isDataLoading.set(true);
        view.showLoader();
        quoteInteractor.loadLiked(this);
    }

    @Override
    public void onFinishLoad(List<QuoteInfo> quotes, boolean fromCache)
    {
        view.showLikedQuotes(quotes);
        view.hideLoader();

        isDataLoading.set(false);
    }

    @Override
    public void onLoadError(Throwable t, boolean fromCache)
    {
        isDataLoading.set(false);
        view.hideLoader();
    }
}
