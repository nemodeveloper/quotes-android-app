package ru.nemodev.project.quotes.mvp.purchase;

import com.anjlab.android.iab.v3.SkuDetails;

public interface OnPurchaseClickListener
{
    void onSkuClick(SkuDetails skuDetails);
}
