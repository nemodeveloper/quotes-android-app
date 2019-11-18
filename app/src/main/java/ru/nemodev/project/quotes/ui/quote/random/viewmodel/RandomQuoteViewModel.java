package ru.nemodev.project.quotes.ui.quote.random.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import org.apache.commons.collections4.CollectionUtils;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteService;
import ru.nemodev.project.quotes.ui.quote.random.source.RandomQuoteSourceFactory;

public class RandomQuoteViewModel extends ViewModel {

    public final LiveData<PagedList<QuoteInfo>> randomQuoteList;
    private final RandomQuoteSourceFactory factory;

    public RandomQuoteViewModel() {
        factory = new RandomQuoteSourceFactory(QuoteService.getInstance());
        randomQuoteList = new LivePagedListBuilder<>(
                factory,
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(200)
                        .setPrefetchDistance(50)
                        .build())
                .build();
    }

    public void onInternetEvent(boolean isAvailable) {
        if (isAvailable && randomQuoteList.getValue() != null
                && CollectionUtils.isEmpty(randomQuoteList.getValue().snapshot())) {
            refresh();
        }
    }

    public void refresh() {
        factory.liveData.getValue().invalidate();
    }
}
