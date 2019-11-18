package ru.nemodev.project.quotes.ui.category.detail.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;
import ru.nemodev.project.quotes.service.quote.QuoteService;
import ru.nemodev.project.quotes.ui.base.BaseViewModel;


public class QuoteByCategoryViewModel extends BaseViewModel {

    private LiveData<PagedList<QuoteInfo>> quoteByCategoryList;
    private Long categoryId;

    public QuoteByCategoryViewModel() { }

    public LiveData<PagedList<QuoteInfo>> getQuoteByCategoryList(Long categoryId) {

        if (quoteByCategoryList == null) {
            this.categoryId = categoryId;

            quoteByCategoryList = new LivePagedListBuilder<>(
                    AppDataBase.getInstance().getQuoteRepository().getByCategoryId(categoryId),
                    new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setPageSize(500)
                            .setPrefetchDistance(100)
                            .build())
                    .build();
        }

        return quoteByCategoryList;
    }

    public void onInternetEvent(boolean isAvailable) {
        if (!synced.get() && isAvailable) {
            startWorkEvent.postValue(true);
            QuoteService.getInstance().syncWithServerByCategory(categoryId)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) { }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            synced.set(aBoolean);
                            startWorkEvent.postValue(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            startWorkEvent.postValue(false);
                        }

                        @Override
                        public void onComplete() { }
                    });
        }
    }

    public void refresh() {
        if (quoteByCategoryList.getValue() != null) {
            startWorkEvent.postValue(true);
            quoteByCategoryList.getValue().getDataSource().invalidate();
        }
    }

}
