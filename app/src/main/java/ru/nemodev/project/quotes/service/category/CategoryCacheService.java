package ru.nemodev.project.quotes.service.category;

import android.support.v4.util.LruCache;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.api.RetrofitAPIFactory;
import ru.nemodev.project.quotes.entity.Category;
import ru.nemodev.project.quotes.utils.CategoryUtils;

public class CategoryCacheService
{
    private static final String CATEGORY_GET_ALL_CACHE_KEY = "CATEGORY_GET_ALL";

    private static volatile CategoryCacheService instance;

    private final LruCache<String, List<Category>> categoryCache;

    private CategoryCacheService(int maxSize)
    {
        categoryCache = new LruCache<>(maxSize);
    }

    public static CategoryCacheService getInstance()
    {
        if (instance == null)
        {
            synchronized (CategoryCacheService.class)
            {
                if (instance == null)
                    instance = new CategoryCacheService(1);
            }
        }

        return instance;
    }

    public Observable<List<Category>> getAll()
    {
        synchronized (CategoryCacheService.class)
        {
            List<Category> cachedAuthors = categoryCache.get(CATEGORY_GET_ALL_CACHE_KEY);
            if (cachedAuthors == null)
            {
                Observable<List<Category>> observable = RetrofitAPIFactory.getCategoryAPI().getAll()
                        .map(CategoryUtils::convertCategories)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

                observable.subscribe(new Observer<List<Category>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    { }

                    @Override
                    public void onNext(List<Category> categories)
                    {
                        categoryCache.put(CATEGORY_GET_ALL_CACHE_KEY , categories);
                    }

                    @Override
                    public void onError(Throwable e)
                    { }

                    @Override
                    public void onComplete()
                    { }
                });

                return observable;
            }

            return Observable.just(cachedAuthors);
        }
    }
}