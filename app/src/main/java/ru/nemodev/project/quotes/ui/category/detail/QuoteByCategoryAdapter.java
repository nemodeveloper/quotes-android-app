package ru.nemodev.project.quotes.ui.category.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.databinding.QuoteByCategoryCardViewBinding;
import ru.nemodev.project.quotes.ui.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.ui.base.OnQuoteCardClickListener;

public class QuoteByCategoryAdapter extends BaseQuoteAdapter<QuoteByCategoryAdapter.QuoteByCategoryViewHolder> {

    public QuoteByCategoryAdapter(Context context, OnQuoteCardClickListener onQuoteCardClickListener) {
        super(context, onQuoteCardClickListener);
    }

    public static class QuoteByCategoryViewHolder extends RecyclerView.ViewHolder {
        public final QuoteByCategoryCardViewBinding binding;

        public QuoteByCategoryViewHolder(QuoteByCategoryCardViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public QuoteByCategoryAdapter.QuoteByCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuoteByCategoryAdapter.QuoteByCategoryViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(context),
                        R.layout.quote_by_category_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteByCategoryAdapter.QuoteByCategoryViewHolder viewHolder, int position) {
        viewHolder.binding.setQuoteAdapter(this);
        viewHolder.binding.setQuoteInfo(getItem(position));
    }
}