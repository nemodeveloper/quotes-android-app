package ru.nemodev.project.quotes.ui.category.detail.source;

import androidx.annotation.NonNull;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteService;
import ru.nemodev.project.quotes.ui.base.BaseQuoteDataSource;

public class QuoteByCategoryDataSource extends BaseQuoteDataSource {

    private final Long categoryId;

    public QuoteByCategoryDataSource(Long categoryId, QuoteService quoteService) {
        super(quoteService);
        this.categoryId = categoryId;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<QuoteInfo> callback) {
       quoteService.getByCategory(categoryId)
                .subscribe(new Observer<List<QuoteInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<QuoteInfo> quoteInfos) {
                        if (CollectionUtils.isEmpty(quoteInfos)) {
                            callback.onResult(Collections.emptyList(), 0);
                        }
                        else {
                            callback.onResult(quoteInfos, params.requestedStartPosition);
                        }
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<QuoteInfo> callback) {
        callback.onResult(Collections.emptyList());
    }
}
