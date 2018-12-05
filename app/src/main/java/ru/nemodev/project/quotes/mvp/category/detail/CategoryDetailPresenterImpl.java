package ru.nemodev.project.quotes.mvp.category.detail;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.external.Quote;


public class CategoryDetailPresenterImpl implements CategoryDetailContract.CategoryDetailPresenter, CategoryDetailContract.CategoryDetailIntractor.OnFinishLoadListener
{
    private final CategoryDetailContract.CategoryDetailView view;
    private final CategoryDetailContract.CategoryDetailIntractor model;
    private final Long categoryId;

    private volatile AtomicBoolean isAllDataLoaded = new AtomicBoolean(false);
    private volatile AtomicBoolean isDataLoading = new AtomicBoolean(false);

    public CategoryDetailPresenterImpl(Long categoryId, CategoryDetailContract.CategoryDetailView view)
    {
        this.categoryId = categoryId;
        this.view = view;
        this.model = new CategoryDetailIntractorImpl();
    }

    @Override
    public void loadQuotes()
    {
        if (isAllDataLoaded.get() || isDataLoading.get())
            return;

        isDataLoading.set(true);

        view.showLoader();
        model.loadQuotes(categoryId, this);
    }

    @Override
    public void onFinishLoad(List<Quote> quotes)
    {
        Collections.shuffle(quotes);

        view.showQuotes(quotes);
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
                        loadQuotes();
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }
}
