package ru.nemodev.project.quotes.mvp.quote.liked;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.mvp.base.BaseQuoteCardView;
import ru.nemodev.project.quotes.mvp.base.EmptyAdapterDataListener;
import ru.nemodev.project.quotes.mvp.base.OnQuoteCardClickListener;
import ru.nemodev.project.quotes.utils.MetricUtils;

public class LikedQuoteListAdapter extends BaseQuoteAdapter
{
    private final EmptyAdapterDataListener emptyAdapterDataListener;

    public LikedQuoteListAdapter(FragmentActivity fragmentActivity, List<QuoteInfo> quotes,
                                 OnQuoteCardClickListener onQuoteCardClickListener,
                                 EmptyAdapterDataListener emptyAdapterDataListener)
    {
        super(fragmentActivity, quotes, onQuoteCardClickListener);
        this.emptyAdapterDataListener = emptyAdapterDataListener;
    }

    @Override
    protected int getCardViewLayoutId()
    {
        return R.layout.quote_base_card_view;
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
                MetricUtils.rateEvent(MetricUtils.RateType.QUOTE_UNLIKE);
                data.remove(baseQuoteViewHolder.getAdapterPosition());
                notifyItemRemoved(baseQuoteViewHolder.getAdapterPosition());

                if (CollectionUtils.isEmpty(data))
                {
                    emptyAdapterDataListener.onEmpty();
                }
            }
        });
    }
}