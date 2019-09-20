package ru.nemodev.project.quotes.mvp.category.list;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.entity.category.Category;
import ru.nemodev.project.quotes.service.category.CategoryService;

public class CategoryListInteractorImpl implements CategoryListContract.CategoryListInteractor
{
    @Override
    public void loadCategories(OnFinishLoadListener onFinishLoadListener)
    {
        CategoryService.getInstance().getAll()
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
}
