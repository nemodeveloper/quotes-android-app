package ru.nemodev.project.quotes.mvp.base;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ru.nemodev.project.quotes.core.recyclerView.AnimationRVAdapter;
import ru.nemodev.project.quotes.entity.QuoteInfo;

public abstract class BaseQuoteAdapter extends AnimationRVAdapter<QuoteInfo, BaseQuoteAdapter.BaseQuoteViewHolder>
{
    private final FragmentActivity fragmentActivity;

    public BaseQuoteAdapter(FragmentActivity fragmentActivity, List<QuoteInfo> quotes)
    {
        super(fragmentActivity, quotes);
        this.fragmentActivity = fragmentActivity;
    }

    public static class BaseQuoteViewHolder extends RecyclerView.ViewHolder
    {
        private final BaseQuoteCardView quoteCardView;

        public BaseQuoteViewHolder(FragmentActivity fragmentActivity, BaseQuoteCardView itemView)
        {
            super(itemView);
            this.quoteCardView = itemView;
            this.quoteCardView.setFragmentActivity(fragmentActivity);
        }

        public void setQuote(QuoteInfo quote)
        {
            quoteCardView.setQuote(quote);
        }

        public void setOnLikeQuoteEvent(BaseQuoteCardView.OnLikeQuoteEvent onLikeQuoteEvent)
        {
            quoteCardView.setOnLikeQuoteEvent(onLikeQuoteEvent);
        }
    }

    @NonNull
    @Override
    public BaseQuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new BaseQuoteViewHolder(fragmentActivity,
                (BaseQuoteCardView) LayoutInflater.from(context).inflate(getCardViewLayoutId(), parent, false));
    }

    @Override
    protected void doOnBindViewHolder(@NonNull BaseQuoteViewHolder baseQuoteViewHolder, int position)
    {
        baseQuoteViewHolder.setQuote(getItem(position));
    }

    protected abstract int getCardViewLayoutId();
}