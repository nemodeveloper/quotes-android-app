package ru.nemodev.project.quotes.ui.quote.liked;

import android.content.Context;

import androidx.annotation.NonNull;

import org.apache.commons.collections4.CollectionUtils;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.ui.base.BaseQuoteCardView;
import ru.nemodev.project.quotes.ui.base.EmptyAdapterDataListener;
import ru.nemodev.project.quotes.ui.base.OnQuoteCardClickListener;
import ru.nemodev.project.quotes.utils.MetricUtils;

public class LikedQuoteListAdapter extends BaseQuoteAdapter
{
    private final EmptyAdapterDataListener emptyAdapterDataListener;

    public LikedQuoteListAdapter(Context context, OnQuoteCardClickListener onQuoteCardClickListener,
                                 EmptyAdapterDataListener emptyAdapterDataListener)
    {
        super(context, onQuoteCardClickListener);
        this.emptyAdapterDataListener = emptyAdapterDataListener;
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
                // TODO разобраться как удалять из PagedList
//                data.remove(baseQuoteViewHolder.getAdapterPosition());
                notifyItemRemoved(baseQuoteViewHolder.getAdapterPosition());

                if (getCurrentList() == null || CollectionUtils.isEmpty(getCurrentList().snapshot()))
                {
                    emptyAdapterDataListener.onEmptyAdapterData();
                }
            }
        });
    }
}