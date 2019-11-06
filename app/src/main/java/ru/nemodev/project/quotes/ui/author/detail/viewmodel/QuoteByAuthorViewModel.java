package ru.nemodev.project.quotes.ui.author.detail.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteService;
import ru.nemodev.project.quotes.ui.author.detail.source.QuoteByAuthorDataSource;

public class QuoteByAuthorViewModel extends ViewModel {

    private LiveData<PagedList<QuoteInfo>> quoteByAuthorList;

    public QuoteByAuthorViewModel() { }

    public LiveData<PagedList<QuoteInfo>> getQuoteByAuthorList(Long authorId) {

        if (quoteByAuthorList == null) {
            DataSource.Factory<Integer, QuoteInfo> factFactory = new DataSource.Factory<Integer, QuoteInfo>() {
                @NonNull
                @Override
                public DataSource<Integer, QuoteInfo> create() {
                    return new QuoteByAuthorDataSource(authorId, QuoteService.getInstance());
                }
            };

            quoteByAuthorList = new LivePagedListBuilder<>(
                    factFactory,
                    new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setPageSize(10)
                            .setPrefetchDistance(5)
                            .build())
                    .build();
        }

        return quoteByAuthorList;
    }

}
