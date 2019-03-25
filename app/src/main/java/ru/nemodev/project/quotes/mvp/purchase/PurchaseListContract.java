package ru.nemodev.project.quotes.mvp.purchase;

import com.anjlab.android.iab.v3.SkuDetails;

import java.util.List;

public interface PurchaseListContract
{
    interface SkuInAppListView
    {
        void showLoader();
        void hideLoader();
        void showSkuList(List<SkuDetails> skuDetailsList);
    }

    interface SkuInAppListPresenter
    {
        void loadSkuList();
        void onSkuClick(SkuDetails skuDetails);
    }
}
