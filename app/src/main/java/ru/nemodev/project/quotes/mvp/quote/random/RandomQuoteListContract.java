package ru.nemodev.project.quotes.mvp.quote.random;

import java.util.List;

import ru.nemodev.project.quotes.entity.QuoteInfo;

public interface RandomQuoteListContract
{
    interface RandomQuoteListView
    {
        void showLoader();
        void hideLoader();
        void showNextQuotes(List<QuoteInfo> quotes);
    }

    interface RandomQuoteListPresenter
    {
        void loadNextQuotes();
    }

    interface RandomQuoteListIntractor
    {
        interface OnFinishLoadListener
        {
            void onFinishLoad(List<QuoteInfo> quotes);
            void onLoadError(Throwable t);
        }

        void loadQuotes(OnFinishLoadListener onFinishLoadListener);
    }
}
