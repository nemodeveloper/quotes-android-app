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
}
