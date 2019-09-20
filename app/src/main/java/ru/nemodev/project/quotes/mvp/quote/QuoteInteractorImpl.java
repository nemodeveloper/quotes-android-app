package ru.nemodev.project.quotes.mvp.quote;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;
import ru.nemodev.project.quotes.service.quote.QuoteService;

public class QuoteInteractorImpl implements QuoteInteractor
{
    @Override
    public void loadRandom(OnFinishLoadListener onFinishLoadListener, Integer count)
    {
        QuoteService.getInstance().getRandom(count)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<QuoteInfo>>()
            {
                @Override
                public void onSubscribe(Disposable d) { }

                @Override
                public void onNext(List<QuoteInfo> quoteInfoList)
                {
                    onFinishLoadListener.onFinishLoad(quoteInfoList, false);
                }

                @Override
                public void onError(Throwable e)
                {
                    onFinishLoadListener.onLoadError(e, false);
                }

                @Override
                public void onComplete() { }
            });
    }

    @Override
    public void loadByAuthor(OnFinishLoadListener onFinishLoadListener, Long authorId)
    {
        QuoteService.getInstance().getByAuthor(authorId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<QuoteInfo>>()
            {
                @Override
                public void onSubscribe(Disposable d) { }

                @Override
                public void onNext(List<QuoteInfo> quoteInfoList)
                {
                    onFinishLoadListener.onFinishLoad(quoteInfoList, false);
                }

                @Override
                public void onError(Throwable e)
                {
                    onFinishLoadListener.onLoadError(e, false);
                }

                @Override
                public void onComplete() { }
            });
    }

    @Override
    public void loadByCategory(OnFinishLoadListener onFinishLoadListener, Long categoryId)
    {
        QuoteService.getInstance().getByCategory(categoryId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<QuoteInfo>>()
            {
                @Override
                public void onSubscribe(Disposable d) { }

                @Override
                public void onNext(List<QuoteInfo> quoteInfoList)
                {
                    onFinishLoadListener.onFinishLoad(quoteInfoList, false);
                }

                @Override
                public void onError(Throwable e)
                {
                    onFinishLoadListener.onLoadError(e, false);
                }

                @Override
                public void onComplete() { }
            });
    }

    @Override
    public void loadLiked(OnFinishLoadListener onFinishLoadListener)
    {
        AppDataBase.getInstance().getQuoteRepository().getLiked()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<QuoteInfo>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(List<QuoteInfo> quoteInfoList)
                    {
                        onFinishLoadListener.onFinishLoad(quoteInfoList, true);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onFinishLoadListener.onLoadError(e, true);
                    }
                });
    }

    @Override
    public Single<QuoteInfo> getById(Long quoteId)
    {
        return AppDataBase.getInstance().getQuoteRepository().getById(quoteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
