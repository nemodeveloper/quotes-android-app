package ru.nemodev.project.quotes.view.quote.author;

import android.support.v4.app.FragmentActivity;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.view.base.BaseQuoteAdapter;

public class QuoteByAuthorAdapter extends BaseQuoteAdapter
{
    private static final int INITIAL_QUOTE_SIZE = 100;

    public QuoteByAuthorAdapter(FragmentActivity fragmentActivity)
    {
        super(fragmentActivity, INITIAL_QUOTE_SIZE);
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_by_author_card_item;
    }
}