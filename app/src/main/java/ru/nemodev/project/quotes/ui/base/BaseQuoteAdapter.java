package ru.nemodev.project.quotes.ui.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;

public abstract class BaseQuoteAdapter extends PagedListAdapter<QuoteInfo, BaseQuoteAdapter.BaseQuoteViewHolder>
{
    private static DiffUtil.ItemCallback<QuoteInfo> DIFF_CALLBACK = new DiffUtil.ItemCallback<QuoteInfo>() {
        @Override
        public boolean areItemsTheSame(@NonNull QuoteInfo oldItem, @NonNull QuoteInfo newItem) {
            return oldItem.getQuote().getId().equals(newItem.getQuote().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull QuoteInfo oldItem, @NonNull QuoteInfo newItem) {
            return oldItem.equals(newItem);
        }
    };

    private final OnQuoteCardClickListener onQuoteCardClickListener;
    protected final Context context;

    public BaseQuoteAdapter(Context context, OnQuoteCardClickListener onQuoteCardClickListener)
    {
        super(DIFF_CALLBACK);
        this.context = context;
        this.onQuoteCardClickListener = onQuoteCardClickListener;
    }

    public static class BaseQuoteViewHolder extends RecyclerView.ViewHolder
    {
        private final BaseQuoteCardView quoteCardView;

        public BaseQuoteViewHolder(BaseQuoteCardView itemView)
        {
            super(itemView);
            this.quoteCardView = itemView;
        }

        public void setQuote(QuoteInfo quote)
        {
            quoteCardView.setQuote(quote);
        }

        public void setOnLikeQuoteEvent(BaseQuoteCardView.OnLikeQuoteListener onLikeQuoteListener)
        {
            quoteCardView.setOnLikeQuoteListener(onLikeQuoteListener);
        }
    }

    @NonNull
    @Override
    public BaseQuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        BaseQuoteCardView baseQuoteCardView = (BaseQuoteCardView) LayoutInflater.from(context).inflate(getCardViewLayoutId(), parent, false);
        baseQuoteCardView.setOnQuoteCardClickListener(onQuoteCardClickListener);

        return new BaseQuoteViewHolder(baseQuoteCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseQuoteViewHolder baseQuoteViewHolder, int position)
    {
        baseQuoteViewHolder.setQuote(getItem(position));
    }

    protected abstract int getCardViewLayoutId();
}