package ru.nemodev.project.quotes.ui.quote.random;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.databinding.QuoteBaseCardViewBinding;
import ru.nemodev.project.quotes.ui.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.ui.base.OnQuoteCardClickListener;


public class RandomQuoteListAdapter extends BaseQuoteAdapter<RandomQuoteListAdapter.BaseQuoteViewHolder> {

    public RandomQuoteListAdapter(Context context, OnQuoteCardClickListener onQuoteCardClickListener) {
        super(context, onQuoteCardClickListener);
    }

    public static class BaseQuoteViewHolder extends RecyclerView.ViewHolder {
        public final QuoteBaseCardViewBinding binding;

        public BaseQuoteViewHolder(QuoteBaseCardViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public BaseQuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseQuoteViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quote_base_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseQuoteViewHolder viewHolder, int position) {
        viewHolder.binding.setQuoteAdapter(this);
        viewHolder.binding.setQuoteInfo(getItem(position));
    }
}