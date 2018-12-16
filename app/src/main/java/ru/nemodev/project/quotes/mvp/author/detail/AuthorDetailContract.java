package ru.nemodev.project.quotes.mvp.author.detail;

import java.util.List;

import ru.nemodev.project.quotes.entity.QuoteInfo;

public interface AuthorDetailContract
{
    interface AuthorDetailView
    {
        void showLoader();
        void hideLoader();
        void showQuotes(List<QuoteInfo> quotes);
    }

    interface AuthorDetailPresenter
    {
        void loadQuotes();
    }

    interface AuthorDetailIntractor
    {
        interface OnFinishLoadListener
        {
            void onFinishLoad(List<QuoteInfo> quotes, boolean fromCache);
            void onLoadError(Throwable t);
        }

        void loadQuotes(Long authorId, OnFinishLoadListener onFinishLoadListener, boolean fromCache);
    }
}
