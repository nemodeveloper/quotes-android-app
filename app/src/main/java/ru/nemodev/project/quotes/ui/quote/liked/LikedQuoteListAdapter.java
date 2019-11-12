package ru.nemodev.project.quotes.ui.quote.liked;

import android.content.Context;

import androidx.annotation.NonNull;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.ui.base.BaseQuoteCardView;
import ru.nemodev.project.quotes.ui.base.OnQuoteCardClickListener;
import ru.nemodev.project.quotes.utils.MetricUtils;

public class LikedQuoteListAdapter extends BaseQuoteAdapter
{

    public LikedQuoteListAdapter(Context context, OnQuoteCardClickListener onQuoteCardClickListener)
    {
        super(context, onQuoteCardClickListener);
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_base_card_view;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseQuoteViewHolder baseQuoteViewHolder, int position)
    {
        super.onBindViewHolder(baseQuoteViewHolder, position);

        baseQuoteViewHolder.setOnLikeQuoteEvent(new BaseQuoteCardView.OnLikeQuoteListener()
        {
            @Override
            public void like() { }

            @Override
            public void unLike()
            {
                MetricUtils.rateEvent(MetricUtils.RateType.QUOTE_UNLIKE);
            }
        });
    }
}