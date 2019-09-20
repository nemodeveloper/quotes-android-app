package ru.nemodev.project.quotes.repository.cache.author;

import androidx.collection.LruCache;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.author.Author;


public class AuthorCacheRepositoryImpl implements AuthorCacheRepository
{
    private static final String AUTHOR_GET_ALL_CACHE_KEY = "AUTHOR_GET_ALL";

    private final LruCache<String, List<Author>> authorListCache;

    public AuthorCacheRepositoryImpl()
    {
        authorListCache = new LruCache<>(1);
    }

    @Override
    public Observable<List<Author>> getAll()
    {
        List<Author> cachedAuthors = authorListCache.get(AUTHOR_GET_ALL_CACHE_KEY);
        if (cachedAuthors == null)
            cachedAuthors = Collections.emptyList();

        return Observable.just(cachedAuthors)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void putAll(List<Author> authorList)
    {
        authorListCache.put(AUTHOR_GET_ALL_CACHE_KEY, authorList);
    }

}
