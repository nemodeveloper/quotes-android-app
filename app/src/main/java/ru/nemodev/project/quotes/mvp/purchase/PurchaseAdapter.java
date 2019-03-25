package ru.nemodev.project.quotes.mvp.purchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.anjlab.android.iab.v3.SkuDetails;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.recyclerView.AnimationRVAdapter;

public class PurchaseAdapter extends AnimationRVAdapter<SkuDetails, PurchaseAdapter.PurchaseViewHolder>
{
    private final OnPurchaseClickListener onPurchaseClickListener;

    public PurchaseAdapter(Context context, List<SkuDetails> data, OnPurchaseClickListener onPurchaseClickListener)
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
        PurchaseCardView purchaseCardView = (PurchaseCardView) LayoutInflater.from(context).inflate(R.layout.purchase_card_item, parent, false);

        return new PurchaseViewHolder(purchaseCardView);
    }

    @Override
    protected void doOnBindViewHolder(@NonNull PurchaseViewHolder purchaseViewHolder, int position)
    {
        final SkuDetails skuDetails = getItem(position);

        purchaseViewHolder.purchaseCardView.setSkuDetails(skuDetails);
        purchaseViewHolder.purchaseCardView.setOnClickListener(v -> onPurchaseClickListener.onSkuClick(skuDetails));
    }
}
