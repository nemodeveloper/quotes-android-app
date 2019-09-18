package ru.nemodev.project.quotes.mvp.category.list;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nemodev.project.quotes.entity.category.Category;


public class CategoryListPresenterImpl implements CategoryListContract.CategoryListPresenter, CategoryListContract.CategoryListInteractor.OnFinishLoadListener
{
    private final CategoryListContract.CategoryListView view;
    private final CategoryListContract.CategoryListInteractor model;

    private volatile AtomicBoolean isAllDataLoaded = new AtomicBoolean(false);
    private volatile AtomicBoolean isDataLoading = new AtomicBoolean(false);

    public CategoryListPresenterImpl(CategoryListContract.CategoryListView view)
    {
        this.view = view;
        this.model = new CategoryListInteractorImpl();
    }

    @Override
    public void loadCategory()
    {
        if (isAllDataLoaded.get() || isDataLoading.get())
            return;

        isDataLoading.set(true);

        view.showLoader();
        model.loadCategories(this, false);
    }

    @Override
    public void onFinishLoad(List<Category> categories, boolean fromCache)
    {
        view.showCategories(categories);
        view.hideLoader();

        isAllDataLoaded.set(!fromCache);
        isDataLoading.set(false);
    }

    @Override
    public void onLoadError(Throwable t)
    {
        isDataLoading.set(false);
        model.loadCategories(this, true);
    }
}
