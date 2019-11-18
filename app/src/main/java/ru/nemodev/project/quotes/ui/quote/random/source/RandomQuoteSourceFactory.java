package ru.nemodev.project.quotes.ui.quote.random.source;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteService;

public class RandomQuoteSourceFactory extends DataSource.Factory<Integer, QuoteInfo> {

    public final MutableLiveData<RandomQuoteDataSource> liveData;
    private final QuoteService quoteService;

    public RandomQuoteSourceFactory(QuoteService quoteService) {
        this.quoteService = quoteService;
        this.liveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource<Integer, QuoteInfo> create() {
        RandomQuoteDataSource dataSource = new RandomQuoteDataSource(quoteService);
        liveData.postValue(dataSource);
        return dataSource;
    }
}
