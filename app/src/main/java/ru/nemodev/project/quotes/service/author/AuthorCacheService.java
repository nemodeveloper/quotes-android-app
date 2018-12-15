package ru.nemodev.project.quotes.service.author;

import android.support.v4.util.LruCache;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.api.RetrofitAPIFactory;
import ru.nemodev.project.quotes.entity.Author;
import ru.nemodev.project.quotes.utils.AuthorUtils;

public class AuthorCacheService
{
    private static final String AUTHOR_GET_ALL_CACHE_KEY = "AUTHOR_GET_ALL";

    private static volatile AuthorCacheService instance;

    private final LruCache<String, List<Author>> authorCache;

    private AuthorCacheService(int maxSize)
    {
        authorCache = new LruCache<>(maxSize);
    }

    public static AuthorCacheService getInstance()
    {
        if (instance == null)
        {
            synchronized (AuthorCacheService.class)
            {
                if (instance == null)
                    instance = new AuthorCacheService(1);
            }
        }

        return instance;
    }

    public Observable<List<Author>> getAll()
    {
        synchronized (AuthorCacheService.class)
        {
            List<Author> cachedAuthors = authorCache.get(AUTHOR_GET_ALL_CACHE_KEY);
            if (cachedAuthors == null)
            {
                Observable<List<Author>> authorObservable = RetrofitAPIFactory.getAuthorAPI().getAll()
                        .map(AuthorUtils::convertAuthors)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

                authorObservable.subscribe(new Observer<List<Author>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    { }

                    @Override
                    public void onNext(List<Author> authors)
                    {
                        authorCache.put(AUTHOR_GET_ALL_CACHE_KEY, authors);
                    }

                    @Override
                    public void onError(Throwable e)
                    { }

                    @Override
                    public void onComplete()
                    { }
                });

                return authorObservable;
            }

            return Observable.just(cachedAuthors);
        }
    }
}