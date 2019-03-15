package ru.nemodev.project.quotes.mvp.quote.liked;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.mvp.base.BaseQuoteCardView;
import ru.nemodev.project.quotes.mvp.base.OnQuoteCardClickListener;

public class LikedQuoteListAdapter extends BaseQuoteAdapter
{
    public LikedQuoteListAdapter(FragmentActivity fragmentActivity, List<QuoteInfo> quotes, OnQuoteCardClickListener onQuoteCardClickListener)
    {
        super(fragmentActivity, quotes, onQuoteCardClickListener);
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_base_card_item;
    }

    @Override
    protected void doOnBindViewHolder(@NonNull BaseQuoteViewHolder baseQuoteViewHolder, int position)
    {
        super.doOnBindViewHolder(baseQuoteViewHolder, position);

        baseQuoteViewHolder.setOnLikeQuoteEvent(new BaseQuoteCardView.OnLikeQuoteListener()
        {
            @Override
            public void like() { }

            @Override
            public void unLike()
            {
                data.remove(baseQuoteViewHolder.getAdapterPosition());
                notifyItemRemoved(baseQuoteViewHolder.getAdapterPosition());
            }
        });
    }
}