package ru.nemodev.project.quotes.mvp.purchase;

public interface BillingEventListener
{
    void onPurchase(String productId);
}
