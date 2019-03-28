package ru.nemodev.project.quotes.mvp.author.detail;

import java.util.List;

import androidx.fragment.app.FragmentActivity;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.mvp.base.OnQuoteCardClickListener;

public class AuthorQuotesAdapter extends BaseQuoteAdapter
{
    public AuthorQuotesAdapter(FragmentActivity fragmentActivity, List<QuoteInfo> quotes, OnQuoteCardClickListener onQuoteCardClickListener)
    {
        super(fragmentActivity, quotes, onQuoteCardClickListener);
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_by_author_card_view;
    }
}