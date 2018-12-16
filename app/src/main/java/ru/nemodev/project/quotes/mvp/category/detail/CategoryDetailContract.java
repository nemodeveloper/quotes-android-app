package ru.nemodev.project.quotes.mvp.category.detail;

import java.util.List;

import ru.nemodev.project.quotes.entity.QuoteInfo;

public interface CategoryDetailContract
{
    interface CategoryDetailView
    {
        void showLoader();
        void hideLoader();
        void showQuotes(List<QuoteInfo> quotes);
    }

    interface CategoryDetailPresenter
    {
        void loadQuotes();
    }

    interface CategoryDetailIntractor
    {
        interface OnFinishLoadListener
        {
            void onFinishLoad(List<QuoteInfo> quotes, boolean fromCache);
            void onLoadError(Throwable t);
        }

        void loadQuotes(Long categoryId, OnFinishLoadListener onFinishLoadListener, boolean fromCache);
    }
}
