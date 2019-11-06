package ru.nemodev.project.quotes.ui.author.detail;

import android.content.Context;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.ui.base.OnQuoteCardClickListener;

public class QuoteByAuthorAdapter extends BaseQuoteAdapter
{
    public QuoteByAuthorAdapter(Context context, OnQuoteCardClickListener onQuoteCardClickListener)
    {
        super(context, onQuoteCardClickListener);
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_by_author_card_view;
    }
}