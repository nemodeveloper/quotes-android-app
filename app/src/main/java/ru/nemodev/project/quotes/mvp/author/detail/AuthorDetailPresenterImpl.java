package ru.nemodev.project.quotes.mvp.author.detail;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nemodev.project.quotes.entity.QuoteInfo;


public class AuthorDetailPresenterImpl implements AuthorDetailContract.AuthorDetailPresenter, AuthorDetailContract.AuthorDetailIntractor.OnFinishLoadListener
{
    private final AuthorDetailContract.AuthorDetailView view;
    private final AuthorDetailContract.AuthorDetailIntractor model;
    private final Long authorId;

    private volatile AtomicBoolean isAllDataLoaded = new AtomicBoolean(false);
    private volatile AtomicBoolean isDataLoading = new AtomicBoolean(false);

    public AuthorDetailPresenterImpl(Long authorId, AuthorDetailContract.AuthorDetailView view)
    {
        this.authorId = authorId;
        this.view = view;
        this.model = new AuthorDetailIntractorImpl();
    }

    @Override
    public void loadQuotes()
    {
        if (isAllDataLoaded.get() || isDataLoading.get())
            return;

        isDataLoading.set(true);

        view.showLoader();
        model.loadQuotes(authorId, this, false);
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
    public void onLoadError(Throwable t)
    {
        isDataLoading.set(false);
        model.loadQuotes(authorId, this, true);
    }
}
