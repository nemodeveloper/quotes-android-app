package ru.nemodev.project.quotes.mvp.quote.random;

import java.util.ArrayList;

import androidx.fragment.app.FragmentActivity;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.mvp.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.mvp.base.OnQuoteCardClickListener;

public class RandomQuoteListAdapter extends BaseQuoteAdapter
{
    private static final int INITIAL_QUOTE_SIZE = 1000;

    public RandomQuoteListAdapter(FragmentActivity fragmentActivity, OnQuoteCardClickListener onQuoteCardClickListener)
    {
        super(fragmentActivity, new ArrayList<>(INITIAL_QUOTE_SIZE), onQuoteCardClickListener);
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_base_card_item;
    }
}