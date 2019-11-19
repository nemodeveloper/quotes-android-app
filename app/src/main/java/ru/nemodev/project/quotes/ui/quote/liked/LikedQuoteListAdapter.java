package ru.nemodev.project.quotes.ui.quote.liked;

import android.content.Context;

import ru.nemodev.project.quotes.ui.base.OnQuoteCardClickListener;
import ru.nemodev.project.quotes.ui.quote.random.RandomQuoteListAdapter;

public class LikedQuoteListAdapter extends RandomQuoteListAdapter {

    public LikedQuoteListAdapter(Context context, OnQuoteCardClickListener onQuoteCardClickListener) {
        super(context, onQuoteCardClickListener);
    }
}