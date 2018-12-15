package ru.nemodev.project.quotes.mvp.author.list;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.Author;
import ru.nemodev.project.quotes.service.author.AuthorCacheService;

public class AuthorListIntactorImpl implements AuthorListContract.AuthorListIntractor
{
    @Override
    public void loadAuthors(OnFinishLoadListener onFinishLoadListener)
    {
        final Observer<List<Author>> loadNewItemsSubscriber = new Observer<List<Author>>()
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
            public void onNext(List<Author> items)
            {
                onFinishLoadListener.onFinishLoad(items);
            }
        };

        AuthorCacheService.getInstance().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loadNewItemsSubscriber);
    }
}
