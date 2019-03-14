package ru.nemodev.project.quotes.mvp.quote.liked;

import java.util.List;

import ru.nemodev.project.quotes.entity.QuoteInfo;

public interface LikedQuoteListContract
{
    interface LikedQuoteListView
    {
        void showLoader();
        void hideLoader();
        void showLikedQuotes(List<QuoteInfo> quotes);
    }

    interface LikedQuoteListPresenter
    {
        void loadLikedQuotes();
    }
}
