package ru.nemodev.project.quotes.mvp.quote;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import ru.nemodev.project.quotes.entity.QuoteInfo;

public interface QuoteIntractor
{
    interface OnFinishLoadListener
    {
        void onFinishLoad(List<QuoteInfo> quotes, boolean fromCache);
        void onLoadError(Throwable t, boolean fromCache);
    }

    void loadRandom(OnFinishLoadListener onFinishLoadListener, Map<String, String> params, boolean fromCache);
    void loadByAuthor(OnFinishLoadListener onFinishLoadListener, Long authorId, boolean fromCache);
    void loadByCategory(OnFinishLoadListener onFinishLoadListener, Long categoryId, boolean fromCache);
    void loadLiked(OnFinishLoadListener onFinishLoadListener);

    Single<QuoteInfo> getById(Long quoteId);
}
