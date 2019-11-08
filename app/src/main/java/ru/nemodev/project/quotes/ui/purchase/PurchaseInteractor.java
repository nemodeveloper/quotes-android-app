package ru.nemodev.project.quotes.ui.purchase;


import java.util.List;

import io.reactivex.Observable;
import ru.nemodev.project.quotes.entity.purchase.Purchase;
import ru.nemodev.project.quotes.entity.purchase.PurchaseType;

public interface PurchaseInteractor
{
    Observable<List<Purchase>> loadPurchaseInAppList();
    Observable<Purchase> loadPurchase(String productId);
    void loadOwnedPurchaseList();

    void purchase(PurchaseType purchaseType);
    boolean isPurchase(PurchaseType purchaseType);
}
