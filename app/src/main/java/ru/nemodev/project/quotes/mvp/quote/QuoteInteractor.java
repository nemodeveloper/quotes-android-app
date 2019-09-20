package ru.nemodev.project.quotes.mvp.quote;

import java.util.List;

import io.reactivex.Single;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;

public interface QuoteInteractor
{
    interface OnFinishLoadListener
    {
        void onFinishLoad(List<QuoteInfo> quotes, boolean fromCache);
        void onLoadError(Throwable t, boolean fromCache);
    }

    void loadRandom(OnFinishLoadListener onFinishLoadListener, Integer count);
    void loadByAuthor(OnFinishLoadListener onFinishLoadListener, Long authorId);
    void loadByCategory(OnFinishLoadListener onFinishLoadListener, Long categoryId);
    void loadLiked(OnFinishLoadListener onFinishLoadListener);

    Single<QuoteInfo> getById(Long quoteId);
}
