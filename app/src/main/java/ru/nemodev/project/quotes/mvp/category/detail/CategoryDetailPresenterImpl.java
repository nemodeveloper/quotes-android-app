package ru.nemodev.project.quotes.mvp.category.detail;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nemodev.project.quotes.entity.QuoteInfo;


public class CategoryDetailPresenterImpl implements CategoryDetailContract.CategoryDetailPresenter, CategoryDetailContract.CategoryDetailIntractor.OnFinishLoadListener
{
    private final CategoryDetailContract.CategoryDetailView view;
    private final CategoryDetailContract.CategoryDetailIntractor model;
    private final Long categoryId;

    private volatile AtomicBoolean isAllDataLoaded = new AtomicBoolean(false);
    private volatile AtomicBoolean isDataLoading = new AtomicBoolean(false);

    public CategoryDetailPresenterImpl(Long categoryId, CategoryDetailContract.CategoryDetailView view)
    {
        this.categoryId = categoryId;
        this.view = view;
        this.model = new CategoryDetailIntractorImpl();
    }

    @Override
    public void loadQuotes()
    {
        if (isAllDataLoaded.get() || isDataLoading.get())
            return;

        isDataLoading.set(true);

        view.showLoader();
        model.loadQuotes(categoryId, this, false);
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
        model.loadQuotes(categoryId, this, true);
    }
}
