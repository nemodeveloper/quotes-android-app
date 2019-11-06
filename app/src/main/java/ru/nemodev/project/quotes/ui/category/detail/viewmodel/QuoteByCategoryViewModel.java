package ru.nemodev.project.quotes.ui.category.detail.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteService;
import ru.nemodev.project.quotes.ui.category.detail.source.QuoteByCategoryDataSource;

public class QuoteByCategoryViewModel extends ViewModel {

    private LiveData<PagedList<QuoteInfo>> quoteByCategoryList;

    public QuoteByCategoryViewModel() { }

    public LiveData<PagedList<QuoteInfo>> getQuoteByCategoryList(Long categoryId) {

        if (quoteByCategoryList == null) {
            DataSource.Factory<Integer, QuoteInfo> factFactory = new DataSource.Factory<Integer, QuoteInfo>() {
                @NonNull
                @Override
                public DataSource<Integer, QuoteInfo> create() {
                    return new QuoteByCategoryDataSource(categoryId, QuoteService.getInstance());
                }
            };

            quoteByCategoryList = new LivePagedListBuilder<>(
                    factFactory,
                    new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setPageSize(10)
                            .setPrefetchDistance(5)
                            .build())
                    .build();
        }

        return quoteByCategoryList;
    }

}
