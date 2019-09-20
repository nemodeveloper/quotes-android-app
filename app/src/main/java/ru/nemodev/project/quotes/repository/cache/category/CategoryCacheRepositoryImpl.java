package ru.nemodev.project.quotes.repository.cache.category;

import androidx.collection.LruCache;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.category.Category;


public class CategoryCacheRepositoryImpl implements CategoryCacheRepository {

    private static final String GET_ALL_CACHE_KEY = "GET_ALL";

    private final LruCache<String, List<Category>> listCache;

    public CategoryCacheRepositoryImpl() {
        listCache  = new LruCache<>(1);
    }

    @Override
    public Observable<List<Category>> getAll() {
        List<Category> categories = listCache.get(GET_ALL_CACHE_KEY);
        if (categories == null)
            categories = Collections.emptyList();

        return Observable.just(categories)
                .observeOn(Schedulers.io());
    }

    @Override
    public void putAll(List<Category> categoryList) {
        listCache.put(GET_ALL_CACHE_KEY, categoryList);
    }
}
