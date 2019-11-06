package ru.nemodev.project.quotes.ui.base;

import androidx.paging.PositionalDataSource;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.service.quote.QuoteService;

public abstract class BaseQuoteDataSource extends PositionalDataSource<QuoteInfo> {

    protected final QuoteService quoteService;

    protected BaseQuoteDataSource(QuoteService quoteService) {
        this.quoteService = quoteService;
    }
}
