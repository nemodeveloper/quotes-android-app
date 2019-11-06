package ru.nemodev.project.quotes.ui.category.detail;

import android.content.Context;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.ui.base.OnQuoteCardClickListener;

public class QuoteByCategoryAdapter extends BaseQuoteAdapter
{
    public QuoteByCategoryAdapter(Context context, OnQuoteCardClickListener onQuoteCardClickListener)
    {
        super(context, onQuoteCardClickListener);
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_by_category_card_view;
    }
}