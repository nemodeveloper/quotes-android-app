package ru.nemodev.project.quotes.mvp.quote.liked;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.quote.QuoteIntractor;
import ru.nemodev.project.quotes.mvp.quote.QuoteIntractorImpl;


public class LikedQuoteListPresenterImpl implements
        LikedQuoteListContract.LikedQuoteListPresenter,
        QuoteIntractor.OnFinishLoadListener
{
    private final LikedQuoteListContract.LikedQuoteListView view;
    private final QuoteIntractor quoteIntractor;

    private volatile AtomicBoolean isDataLoading = new AtomicBoolean(false);

    public LikedQuoteListPresenterImpl(LikedQuoteListContract.LikedQuoteListView view)
    {
        this.view = view;
        this.quoteIntractor = new QuoteIntractorImpl();
    }

    @Override
    public void loadLikedQuotes()
    {
        if (isDataLoading.get())
            return;

        isDataLoading.set(true);
        view.showLoader();
        quoteIntractor.loadLiked(this);
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
        view.hideLoader();
        isDataLoading.set(false);
    }
}
