package ru.nemodev.project.quotes.mvp.category.detail;

import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.base.BaseQuoteAdapter;

public class CategoryQuotesAdapter extends BaseQuoteAdapter
{
    public CategoryQuotesAdapter(FragmentActivity fragmentActivity, List<QuoteInfo> quotes)
    {
        super(fragmentActivity, quotes);
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_by_category_card_item;
    }
}