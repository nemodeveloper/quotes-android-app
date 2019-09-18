package ru.nemodev.project.quotes.mvp.category.list;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.category.Category;
import ru.nemodev.project.quotes.repository.database.AppDataBase;
import ru.nemodev.project.quotes.service.category.CategoryCacheService;

public class CategoryListInteractorImpl implements CategoryListContract.CategoryListInteractor
{
    @Override
    public void loadCategories(OnFinishLoadListener onFinishLoadListener, boolean fromCache)
    {
        if (fromCache)
            loadCategoriesFromCache(onFinishLoadListener);
        else
            loadCategoriesFromGate(onFinishLoadListener);
    }

    private void loadCategoriesFromGate(OnFinishLoadListener onFinishLoadListener)
    {
        CategoryCacheService.getInstance().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Category>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<Category> categories)
                    {
                        onFinishLoadListener.onFinishLoad(categories, false);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onFinishLoadListener.onLoadError(e);
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    private void loadCategoriesFromCache(OnFinishLoadListener onFinishLoadListener)
    {
        AppDataBase.getInstance().getCategoryDAO().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Category>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(List<Category> categories)
                    {
                        onFinishLoadListener.onFinishLoad(categories, true);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onFinishLoadListener.onLoadError(e);
                    }
                });
    }
}
