package ru.nemodev.project.quotes.mvp.purchase;

import java.util.List;

import ru.nemodev.project.quotes.entity.purchase.Purchase;

public interface PurchaseListContract
{
    interface SkuInAppListView
    {
        void showLoader();
        void hideLoader();
        void showPurchaseList(List<Purchase> purchaseList);
        void showMessage(String message);
    }

    interface PurchaseInAppListPresenter extends BillingEventListener
    {
        void loadPurchaseList();
        void onPurchaseClick(Purchase purchase);
    }
}
