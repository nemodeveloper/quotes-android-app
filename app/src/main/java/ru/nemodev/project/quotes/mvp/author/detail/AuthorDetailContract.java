package ru.nemodev.project.quotes.mvp.author.detail;

import java.util.List;

import ru.nemodev.project.quotes.entity.external.Quote;

public interface AuthorDetailContract
{
    interface AuthorDetailView
    {
        void showLoader();
        void hideLoader();
        void showQuotes(List<Quote> quotes);
    }

    interface AuthorDetailPresenter
    {
        void loadQuotes();
    }

    interface AuthorDetailIntractor
    {
        interface OnFinishLoadListener
        {
            void onFinishLoad(List<Quote> quotes);
            void onLoadError(Throwable t);
        }

        void loadQuotes(Long authorId, OnFinishLoadListener onFinishLoadListener);
    }
}
