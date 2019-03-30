package ru.nemodev.project.quotes.mvp.quote;

import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.database.AppDataBase;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteCacheService;

public class QuoteIntractorImpl implements QuoteIntractor
{
    @Override
    public void loadRandom(OnFinishLoadListener onFinishLoadListener, Map<String, String> params, boolean fromCache)
    {
        if (fromCache)
        {
            AppDataBase.getInstance().getQuoteDAO().getRandom(Integer.parseInt(params.get("count")))
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
        else
        {
            QuoteCacheService.getInstance().getRandom(params)
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
    }

    @Override
    public void loadByAuthor(OnFinishLoadListener onFinishLoadListener, Long authorId, boolean fromCache)
    {
        if (fromCache)
        {
            AppDataBase.getInstance().getQuoteDAO().getByAuthorId(authorId)
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
        else
        {
            QuoteCacheService.getInstance().getByAuthor(authorId)
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
    }

    @Override
    public void loadByCategory(OnFinishLoadListener onFinishLoadListener, Long categoryId, boolean fromCache)
    {
        if (fromCache)
        {
            AppDataBase.getInstance().getQuoteDAO().getByCategoryId(categoryId)
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
        else
        {
            QuoteCacheService.getInstance().getByCategory(categoryId)
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
    }

    @Override
    public void loadLiked(OnFinishLoadListener onFinishLoadListener)
    {
        AppDataBase.getInstance().getQuoteDAO().getLiked()
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
        return AppDataBase.getInstance().getQuoteDAO().getById(quoteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
