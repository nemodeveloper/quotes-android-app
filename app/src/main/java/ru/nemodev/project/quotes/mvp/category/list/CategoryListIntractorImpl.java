package ru.nemodev.project.quotes.mvp.category.list;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.external.Category;
import ru.nemodev.project.quotes.service.category.CategoryCacheService;

public class CategoryListIntractorImpl implements CategoryListContract.CategoryListIntractor
{
    @Override
    public void loadCategories(OnFinishLoadListener onFinishLoadListener)
    {
        final Observer<List<Category>> loadNewItemsSubscriber = new Observer<List<Category>>()
        {
            @Override
            public void onError(Throwable e)
            {
                onFinishLoadListener.onLoadError(e);
            }

            @Override
            public void onComplete() { }

            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(List<Category> items)
            {
                onFinishLoadListener.onFinishLoad(items);
            }
        };

        CategoryCacheService.getInstance().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loadNewItemsSubscriber);
    }
}
