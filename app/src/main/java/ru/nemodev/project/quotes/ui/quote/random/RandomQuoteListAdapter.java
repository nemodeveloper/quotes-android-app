package ru.nemodev.project.quotes.ui.quote.random;

import android.content.Context;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.ui.base.OnQuoteCardClickListener;

public class RandomQuoteListAdapter extends BaseQuoteAdapter
{
    public RandomQuoteListAdapter(Context context, OnQuoteCardClickListener onQuoteCardClickListener)
    {
        super(context, onQuoteCardClickListener);
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_base_card_view;
    }
}