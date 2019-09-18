package ru.nemodev.project.quotes.mvp.author.list;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.repository.database.AppDataBase;
import ru.nemodev.project.quotes.service.author.AuthorCacheService;


public class AuthorListInteractorImpl implements AuthorListContract.AuthorListInteractor
{
    @Override
    public void loadAuthors(OnFinishLoadListener onFinishLoadListener, boolean fromCache)
    {
        if (fromCache)
            loadAuthorsFromCache(onFinishLoadListener);
        else
            loadAuthorsFromGate(onFinishLoadListener);
    }

    private void loadAuthorsFromGate(OnFinishLoadListener onFinishLoadListener)
    {
        AuthorCacheService.getInstance().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Author>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<Author> authors)
                    {
                        onFinishLoadListener.onFinishLoad(authors, false);
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

    private void loadAuthorsFromCache(OnFinishLoadListener onFinishLoadListener)
    {
        AppDataBase.getInstance().getAuthorDAO().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Author>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

                    @Override
                    public void onSuccess(List<Author> authors)
                    {
                        onFinishLoadListener.onFinishLoad(authors, true);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onFinishLoadListener.onLoadError(e);
                    }
                });
    }

}
