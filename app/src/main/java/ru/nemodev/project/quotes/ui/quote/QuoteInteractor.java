package ru.nemodev.project.quotes.ui.quote;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;

public interface QuoteInteractor
{
    Observable<List<QuoteInfo>> loadRandom(Integer count);
    Observable<List<QuoteInfo>> loadByAuthor(Long authorId);
    Observable<List<QuoteInfo>> loadByCategory(Long categoryId);
    Observable<List<QuoteInfo>> loadLiked();

    Single<QuoteInfo> getById(Long quoteId);
}
