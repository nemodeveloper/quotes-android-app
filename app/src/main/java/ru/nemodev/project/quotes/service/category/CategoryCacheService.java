package ru.nemodev.project.quotes.service.category;


import java.util.List;

import androidx.collection.LruCache;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.api.RetrofitAPIFactory;
import ru.nemodev.project.quotes.database.AppDataBase;
import ru.nemodev.project.quotes.entity.Category;
import ru.nemodev.project.quotes.utils.CategoryUtils;
import ru.nemodev.project.quotes.utils.LogUtils;

public class CategoryCacheService
{
    private static final String LOG_TAG = CategoryCacheService.class.getSimpleName();

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
                        .subscribeOn(Schedulers.io());

                observable.subscribe(new Observer<List<Category>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    { }

                    @Override
                    public void onNext(List<Category> categories)
                    {
                        categoryCache.put(CATEGORY_GET_ALL_CACHE_KEY , categories);
                        saveToDataBase(categories);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        LogUtils.logWithReport(LOG_TAG, "Ошибка сохранения категорий в кеш!", e);
                    }

                    @Override
                    public void onComplete()
                    { }
                });

                return observable;
            }

            return Observable.just(cachedAuthors);
        }
    }

    private void saveToDataBase(List<Category> categories)
    {
        AppDataBase.getInstance().getCategoryDAO().add(categories);
    }
}