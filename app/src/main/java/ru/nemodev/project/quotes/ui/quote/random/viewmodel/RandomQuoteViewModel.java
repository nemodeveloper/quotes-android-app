package ru.nemodev.project.quotes.ui.quote.random.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteService;
import ru.nemodev.project.quotes.ui.quote.random.source.RandomQuoteDataSource;

public class RandomQuoteViewModel extends ViewModel {

    public final LiveData<PagedList<QuoteInfo>> randomQuoteList;

    public RandomQuoteViewModel() {

        DataSource.Factory<Integer, QuoteInfo> factFactory = new DataSource.Factory<Integer, QuoteInfo>() {
            @NonNull
            @Override
            public DataSource<Integer, QuoteInfo> create() {
                return new RandomQuoteDataSource(QuoteService.getInstance());
            }
        };

        randomQuoteList = new LivePagedListBuilder<>(
                factFactory,
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(200)
                        .setPrefetchDistance(50)
                        .build())
                .build();
    }
}
