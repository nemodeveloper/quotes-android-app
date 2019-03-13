package ru.nemodev.project.quotes.mvp.category.detail;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.quote.QuoteIntractor;
import ru.nemodev.project.quotes.mvp.quote.random.QuoteIntractorImpl;


public class CategoryDetailPresenterImpl implements
        CategoryDetailContract.CategoryDetailPresenter,
        QuoteIntractor.OnFinishLoadListener
{
    private final CategoryDetailContract.CategoryDetailView view;
    private final QuoteIntractor quoteIntractor;
    private final Long categoryId;

    private volatile AtomicBoolean isAllDataLoaded = new AtomicBoolean(false);
    private volatile AtomicBoolean isDataLoading = new AtomicBoolean(false);

    public CategoryDetailPresenterImpl(Long categoryId, CategoryDetailContract.CategoryDetailView view)
    {
        this.categoryId = categoryId;
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
        quoteIntractor.loadByCategory(this, categoryId, false);
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
        quoteIntractor.loadByCategory(this, categoryId, true);
    }
}
