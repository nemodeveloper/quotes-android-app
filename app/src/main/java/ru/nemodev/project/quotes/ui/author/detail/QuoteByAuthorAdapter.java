package ru.nemodev.project.quotes.ui.author.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.databinding.QuoteByAuthorCardViewBinding;
import ru.nemodev.project.quotes.ui.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.ui.base.OnQuoteCardClickListener;

public class QuoteByAuthorAdapter extends BaseQuoteAdapter<QuoteByAuthorAdapter.QuoteByAuthorViewHolder> {

    public QuoteByAuthorAdapter(Context context, OnQuoteCardClickListener onQuoteCardClickListener) {
        super(context, onQuoteCardClickListener);
    }

    public static class QuoteByAuthorViewHolder extends RecyclerView.ViewHolder {
        public final QuoteByAuthorCardViewBinding binding;

        public QuoteByAuthorViewHolder(QuoteByAuthorCardViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public QuoteByAuthorAdapter.QuoteByAuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuoteByAuthorAdapter.QuoteByAuthorViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(context),
                        R.layout.quote_by_author_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteByAuthorAdapter.QuoteByAuthorViewHolder viewHolder, int position) {
        viewHolder.binding.setQuoteAdapter(this);
        viewHolder.binding.setQuoteInfo(getItem(position));
    }
}