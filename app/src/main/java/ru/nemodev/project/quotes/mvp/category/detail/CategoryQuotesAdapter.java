package ru.nemodev.project.quotes.mvp.category.detail;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.mvp.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.mvp.base.OnQuoteCardClickListener;

public class CategoryQuotesAdapter extends BaseQuoteAdapter
{
    public CategoryQuotesAdapter(FragmentActivity fragmentActivity, List<QuoteInfo> quotes, OnQuoteCardClickListener onQuoteCardClickListener)
    {
        super(fragmentActivity, quotes, onQuoteCardClickListener);
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_by_category_card_view;
    }
}