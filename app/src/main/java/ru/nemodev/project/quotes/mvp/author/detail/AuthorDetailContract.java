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
}
