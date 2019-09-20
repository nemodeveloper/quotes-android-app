package ru.nemodev.project.quotes.repository.cache.quote;

import java.util.List;

import io.reactivex.Observable;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;

public interface QuoteCacheRepository {

    Observable<List<QuoteInfo>> getByAuthor(Long authorId);
    void putAuthorQuotes(Long authorId, List<QuoteInfo> quoteInfoList);

    Observable<List<QuoteInfo>> getByCategory(Long categoryId);
    void putCategoryQuotes(Long categoryId, List<QuoteInfo> quoteInfoList);
}
