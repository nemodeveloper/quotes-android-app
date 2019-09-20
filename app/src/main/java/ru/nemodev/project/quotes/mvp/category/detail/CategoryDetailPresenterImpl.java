package ru.nemodev.project.quotes.mvp.category.detail;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.mvp.quote.QuoteInteractor;
import ru.nemodev.project.quotes.mvp.quote.QuoteInteractorImpl;


public class CategoryDetailPresenterImpl implements
        CategoryDetailContract.CategoryDetailPresenter,
        QuoteInteractor.OnFinishLoadListener
{
    private final CategoryDetailContract.CategoryDetailView view;
    private final QuoteInteractor quoteInteractor;
    private final Long categoryId;

    private volatile AtomicBoolean isAllDataLoaded = new AtomicBoolean(false);
    private volatile AtomicBoolean isDataLoading = new AtomicBoolean(false);

    public CategoryDetailPresenterImpl(Long categoryId, CategoryDetailContract.CategoryDetailView view)
    {
        this.categoryId = categoryId;
        this.view = view;
        this.quoteInteractor = new QuoteInteractorImpl();
    }

    @Override
    public void loadQuotes()
    {
        if (isAllDataLoaded.get() || isDataLoading.get())
            return;

        isDataLoading.set(true);

        view.showLoader();
        quoteInteractor.loadByCategory(this, categoryId);
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
        isAllDataLoaded.set(false);
        view.hideLoader();
    }
}
