package ru.nemodev.project.quotes.ui.quote.random.source;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteService;
import ru.nemodev.project.quotes.ui.base.BaseQuoteDataSource;

public class RandomQuoteDataSource extends BaseQuoteDataSource {

    public RandomQuoteDataSource(QuoteService quoteService) {
        super(quoteService);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<QuoteInfo> callback) {
        quoteService.getRandom(params.requestedLoadSize)
                .subscribe(new Observer<List<QuoteInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<QuoteInfo> quoteInfos) {
                        callback.onResult(quoteInfos, params.requestedStartPosition);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<QuoteInfo> callback) {
        quoteService.getRandom(params.loadSize)
                .subscribe(new Observer<List<QuoteInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<QuoteInfo> quoteInfos) {
                        callback.onResult(quoteInfos);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }
}
