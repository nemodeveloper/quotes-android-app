package ru.nemodev.project.quotes.mvp.author.detail;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.quote.QuoteIntractor;
import ru.nemodev.project.quotes.mvp.quote.random.QuoteIntractorImpl;


public class AuthorDetailPresenterImpl implements AuthorDetailContract.AuthorDetailPresenter, QuoteIntractor.OnFinishLoadListener
{
    private final AuthorDetailContract.AuthorDetailView view;
    private final QuoteIntractor quoteIntractor;
    private final Long authorId;

    private volatile AtomicBoolean isAllDataLoaded = new AtomicBoolean(false);
    private volatile AtomicBoolean isDataLoading = new AtomicBoolean(false);

    public AuthorDetailPresenterImpl(Long authorId, AuthorDetailContract.AuthorDetailView view)
    {
        this.authorId = authorId;
        this.view = view;
        this.quoteIntractor = new QuoteIntractorImpl();
    }

    @Override
    public void loadQuotes()
    {
        if (isAllDataLoaded.get() || isDataLoading.get())
            return;

        isDataLoading.set(true);

        view.showLoader();
        quoteIntractor.loadByAuthor(this, authorId,  false);
    }

    @Override
    public void onFinishLoad(List<QuoteInfo> quotes, boolean fromCache)
    {
        Collections.shuffle(quotes);

        view.showQuotes(quotes);
        view.hideLoader();

        isAllDataLoaded.set(!fromCache);
        isDataLoading.set(false);
    }

    @Override
    public void onLoadError(Throwable t, boolean fromCache)
    {
        isDataLoading.set(false);
        quoteIntractor.loadByAuthor(this, authorId, true);
    }
}
