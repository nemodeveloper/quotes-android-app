package ru.nemodev.project.quotes.mvp.author.list;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nemodev.project.quotes.entity.author.Author;


public class AuthorListPresenterImpl implements AuthorListContract.AuthorListPresenter, AuthorListContract.AuthorListInteractor.OnFinishLoadListener
{
    private final AuthorListContract.AuthorListView view;
    private final AuthorListContract.AuthorListInteractor model;

    private volatile AtomicBoolean isAllDataLoaded = new AtomicBoolean(false);
    private volatile AtomicBoolean isDataLoading = new AtomicBoolean(false);

    public AuthorListPresenterImpl(AuthorListContract.AuthorListView view)
    {
        this.view = view;
        this.model = new AuthorListInteractorImpl();
    }

    @Override
    public void loadAuthors()
    {
        if (isAllDataLoaded.get() || isDataLoading.get())
            return;

        isDataLoading.set(true);

        view.showLoader();
        model.loadAuthors(this);
    }

    @Override
    public void onFinishLoad(List<Author> authors, boolean fromCache)
    {
        view.showAuthors(authors);
        view.hideLoader();

        isAllDataLoaded.set(!fromCache);
        isDataLoading.set(false);
    }

    @Override
    public void onLoadError(Throwable t)
    {
        isDataLoading.set(false);
        isAllDataLoaded.set(false);
        loadAuthors();
    }
}
