package ru.nemodev.project.quotes.ui.quote.liked.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteService;
import ru.nemodev.project.quotes.ui.quote.liked.source.LikedQuoteDataSource;

public class LikedQuoteViewModel extends ViewModel {

    public final LiveData<PagedList<QuoteInfo>> likedQuoteList;

    public LikedQuoteViewModel() {

        DataSource.Factory<Integer, QuoteInfo> factFactory = new DataSource.Factory<Integer, QuoteInfo>() {
            @NonNull
            @Override
            public DataSource<Integer, QuoteInfo> create() {
                return new LikedQuoteDataSource(QuoteService.getInstance());
            }
        };

        likedQuoteList = new LivePagedListBuilder<>(
                factFactory,
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(10)
                        .setPrefetchDistance(5)
                        .build())
                .build();
    }
}
