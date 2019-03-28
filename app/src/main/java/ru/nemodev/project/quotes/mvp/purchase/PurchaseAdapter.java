package ru.nemodev.project.quotes.mvp.purchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.recyclerView.AnimationRVAdapter;
import ru.nemodev.project.quotes.entity.Purchase;

public class PurchaseAdapter extends AnimationRVAdapter<Purchase, PurchaseAdapter.PurchaseViewHolder>
{
    private final OnPurchaseClickListener onPurchaseClickListener;

    public PurchaseAdapter(Context context, List<Purchase> data, OnPurchaseClickListener onPurchaseClickListener)
    {
        super(context, data);
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
    protected void doOnBindViewHolder(@NonNull PurchaseViewHolder purchaseViewHolder, int position)
    {
        final Purchase purchase = getItem(position);

        purchaseViewHolder.purchaseCardView.setPurchase(purchase);
        purchaseViewHolder.purchaseCardView.setOnClickListener(v -> onPurchaseClickListener.onPurchaseClick(purchase));
    }
}
