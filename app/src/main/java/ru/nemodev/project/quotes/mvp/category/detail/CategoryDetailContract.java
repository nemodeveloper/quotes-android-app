package ru.nemodev.project.quotes.mvp.category.detail;

import java.util.List;

import ru.nemodev.project.quotes.entity.external.Quote;

public interface CategoryDetailContract
{
    interface CategoryDetailView
    {
        void showLoader();
        void hideLoader();
        void showQuotes(List<Quote> quotes);
    }

    interface CategoryDetailPresenter
    {
        void loadQuotes();
    }

    interface CategoryDetailIntractor
    {
        interface OnFinishLoadListener
        {
            void onFinishLoad(List<Quote> quotes);
            void onLoadError(Throwable t);
        }

        void loadQuotes(Long categoryId, OnFinishLoadListener onFinishLoadListener);
    }
}
