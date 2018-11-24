package ru.nemodev.project.quotes.view.base;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.nemodev.project.quotes.core.load.simple.SimpleLoadRVAdapter;
import ru.nemodev.project.quotes.entity.external.Quote;

public abstract class BaseQuoteAdapter extends SimpleLoadRVAdapter<Quote, BaseQuoteAdapter.BaseQuoteViewHolder>
{
    private final FragmentActivity fragmentActivity;

    public BaseQuoteAdapter(FragmentActivity fragmentActivity, int initialDataSize)
    {
        super(fragmentActivity, initialDataSize);
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

        public void setQuote(Quote quote)
        {
            quoteCardView.setQuote(quote);
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
    public void onBindViewHolder(@NonNull BaseQuoteViewHolder holder, int position)
    {
        holder.setQuote(getItem(position));
    }

    protected abstract int getCardViewLayoutId();
}