package ru.nemodev.project.quotes.ui.purchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.databinding.PurchaseCardViewBinding;
import ru.nemodev.project.quotes.entity.purchase.Purchase;


public class PurchaseAdapter extends PagedListAdapter<Purchase, PurchaseAdapter.PurchaseViewHolder> {
    private static DiffUtil.ItemCallback<Purchase> DIFF_CALLBACK = new DiffUtil.ItemCallback<Purchase>() {
        @Override
        public boolean areItemsTheSame(@NonNull Purchase oldItem, @NonNull Purchase newItem) {
            return oldItem.getPurchaseType().equals(newItem.getPurchaseType());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Purchase oldItem, @NonNull Purchase newItem) {
            return oldItem.getPurchaseType().equals(newItem.getPurchaseType());
        }
    };

    private final Context context;
    private final OnPurchaseClickListener onPurchaseClickListener;

    public PurchaseAdapter(Context context, OnPurchaseClickListener onPurchaseClickListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.onPurchaseClickListener = onPurchaseClickListener;
    }

    public static class PurchaseViewHolder extends RecyclerView.ViewHolder {
        final PurchaseCardViewBinding binding;

        public PurchaseViewHolder(@NonNull PurchaseCardViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PurchaseViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.purchase_card_view, parent, false));
    }

    public void onPurchaseClick(Purchase purchase) {
        onPurchaseClickListener.onPurchaseClick(purchase);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder purchaseViewHolder, int position) {
        purchaseViewHolder.binding.setPurchaseAdapter(this);
        purchaseViewHolder.binding.setPurchase(getItem(position));
    }
}
