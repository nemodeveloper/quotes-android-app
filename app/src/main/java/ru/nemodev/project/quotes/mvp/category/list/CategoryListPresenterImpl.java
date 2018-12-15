package ru.nemodev.project.quotes.mvp.category.list;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.Category;


public class CategoryListPresenterImpl implements CategoryListContract.CategoryListPresenter, CategoryListContract.CategoryListIntractor.OnFinishLoadListener
{
    private final CategoryListContract.CategoryListView view;
    private final CategoryListContract.CategoryListIntractor intractor;

    private volatile AtomicBoolean isAllDataLoaded = new AtomicBoolean(false);
    private volatile AtomicBoolean isDataLoading = new AtomicBoolean(false);

    public CategoryListPresenterImpl(CategoryListContract.CategoryListView view)
    {
        this.view = view;
        this.intractor = new CategoryListIntractorImpl();
    }

    @Override
    public void loadCategory()
    {
        if (isAllDataLoaded.get() || isDataLoading.get())
            return;

        isDataLoading.set(true);

        view.showLoader();
        intractor.loadCategories(this);
    }

    @Override
    public void onFinishLoad(List<Category> categories)
    {
        view.showCategories(categories);
        view.hideLoader();

        isAllDataLoaded.set(true);
        isDataLoading.set(false);
    }

    @Override
    public void onLoadError(Throwable t)
    {
        isDataLoading.set(false);

        Observable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Long delay)
                    {
                        loadCategory();
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }
}
