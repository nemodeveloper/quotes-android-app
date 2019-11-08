package ru.nemodev.project.quotes.ui.purchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.purchase.Purchase;

public class PurchaseAdapter extends PagedListAdapter<Purchase, PurchaseAdapter.PurchaseViewHolder>
{
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

    public PurchaseAdapter(Context context, OnPurchaseClickListener onPurchaseClickListener)
    {
        super(DIFF_CALLBACK);
        this.context = context;
        this.onPurchaseClickListener = onPurchaseClickListener;
    }

    public static class PurchaseViewHolder extends RecyclerView.ViewHolder
    {
        final PurchaseCardView purchaseCardView;

        public PurchaseViewHolder(@NonNull PurchaseCardView purchaseCardView)
        {
            super(purchaseCardView);
            this.purchaseCardView = purchaseCardView;
        }
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        PurchaseCardView purchaseCardView = (PurchaseCardView) LayoutInflater.from(context).inflate(R.layout.purchase_card_view, parent, false);

        return new PurchaseViewHolder(purchaseCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder purchaseViewHolder, int position)
    {
        final Purchase purchase = getItem(position);

        purchaseViewHolder.purchaseCardView.setPurchase(purchase, onPurchaseClickListener);
    }
}
