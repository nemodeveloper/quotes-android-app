package ru.nemodev.project.quotes.mvp.author.list;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.Author;


public class AuthorListPresenterImpl implements AuthorListContract.AuthorListPresenter, AuthorListContract.AuthorListIntractor.OnFinishLoadListener
{
    private final AuthorListContract.AuthorListView view;
    private final AuthorListContract.AuthorListIntractor model;

    private volatile AtomicBoolean isAllDataLoaded = new AtomicBoolean(false);
    private volatile AtomicBoolean isDataLoading = new AtomicBoolean(false);

    public AuthorListPresenterImpl(AuthorListContract.AuthorListView view)
    {
        this.view = view;
        this.model = new AuthorListIntactorImpl();
    }

    @Override
    public void loadAuthors()
    {
        // TODO подумать как это обыграть более красиво
        if (isAllDataLoaded.get() || isDataLoading.get())
            return;

        isDataLoading.set(true);

        view.showLoader();
        model.loadAuthors(this);
    }

    @Override
    public void onFinishLoad(List<Author> authors)
    {
        view.showAuthors(authors);
        view.hideLoader();

        isAllDataLoaded.set(true);
        isDataLoading.set(false);
    }

    @Override
    public void onLoadError(Throwable t)
    {
        isDataLoading.set(false);

        Observable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Long delay)
                    {
                        loadAuthors();
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }
}
