package ru.nemodev.project.quotes.view.quote.random;

import android.support.v4.app.FragmentActivity;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.view.base.BaseQuoteAdapter;

public class QuoteByRandomAdapter extends BaseQuoteAdapter
{
    private static final int INITIAL_QUOTE_SIZE = 1000;

    public QuoteByRandomAdapter(FragmentActivity fragmentActivity)
    {
        super(fragmentActivity, INITIAL_QUOTE_SIZE);
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_base_card_item;
    }
}