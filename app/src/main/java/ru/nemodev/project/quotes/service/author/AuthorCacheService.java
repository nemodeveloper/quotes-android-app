package ru.nemodev.project.quotes.service.author;

import androidx.collection.LruCache;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.core.utils.LogUtils;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.entity.author.AuthorUtils;
import ru.nemodev.project.quotes.gateway.RetrofitGatewayFactory;
import ru.nemodev.project.quotes.repository.database.AppDataBase;


public class AuthorCacheService
{
    private static final String LOG_TAG = AuthorCacheService.class.getSimpleName();

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
                Observable<List<Author>> authorObservable = RetrofitGatewayFactory.getAuthorAPI().getAll()
                        .map(AuthorUtils::convertAuthors)
                        .subscribeOn(Schedulers.io());

                authorObservable.subscribe(new Observer<List<Author>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    { }

                    @Override
                    public void onNext(List<Author> authors)
                    {
                        authorCache.put(AUTHOR_GET_ALL_CACHE_KEY, authors);
                        saveToDataBase(authors);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        LogUtils.error(LOG_TAG, "Ошибка сохранения авторов в кеш!", e);
                    }

                    @Override
                    public void onComplete()
                    { }
                });

                return authorObservable;
            }

            return Observable.just(cachedAuthors);
        }
    }

    private void saveToDataBase(List<Author> authors)
    {
        AppDataBase.getInstance().getAuthorDAO().add(authors);
    }
}