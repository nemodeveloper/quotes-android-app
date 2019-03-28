package ru.nemodev.project.quotes.mvp.purchase;


import java.util.List;

import io.reactivex.Observable;
import ru.nemodev.project.quotes.entity.Purchase;

public interface PurchaseModel
{
    Observable<List<Purchase>> loadPurchaseInAppList(List<String> productIds);
    Observable<Purchase> loadPurchase(String productId);
    void loadOwnedPurchaseList();

    void purchase(PurchaseType purchaseType);
    boolean isPurchase(PurchaseType purchaseType);
}
