package ru.nemodev.project.quotes.ui.author.detail.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;
import ru.nemodev.project.quotes.service.quote.QuoteService;
import ru.nemodev.project.quotes.ui.base.BaseViewModel;


public class QuoteByAuthorViewModel extends BaseViewModel {

    private LiveData<PagedList<QuoteInfo>> quoteByAuthorList;
    private Long authorId;

    public QuoteByAuthorViewModel() { }

    public LiveData<PagedList<QuoteInfo>> getQuoteByAuthorList(Long authorId) {

        if (quoteByAuthorList == null) {
            this.authorId = authorId;

            quoteByAuthorList = new LivePagedListBuilder<>(
                    AppDataBase.getInstance().getQuoteRepository().getByAuthorId(authorId),
                    new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setPageSize(500)
                            .setPrefetchDistance(100)
                            .build())
                    .build();
        }

        return quoteByAuthorList;
    }

    public void onInternetEvent(boolean isAvailable) {
        if (!synced.get() && isAvailable) {
            startWorkEvent.postValue(true);
            QuoteService.getInstance().syncWithServerByAuthor(authorId)
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

}
