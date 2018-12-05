package ru.nemodev.project.quotes.mvp.quote.random;

import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.mvp.base.BaseQuoteAdapter;

public class RandomQuoteListAdapter extends BaseQuoteAdapter
{
    private static final int INITIAL_QUOTE_SIZE = 1000;

    public RandomQuoteListAdapter(FragmentActivity fragmentActivity)
    {
        super(fragmentActivity, new ArrayList<>(INITIAL_QUOTE_SIZE));
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_base_card_item;
    }
}