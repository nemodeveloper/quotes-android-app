package ru.nemodev.project.quotes.mvp.purchase;

import com.anjlab.android.iab.v3.SkuDetails;

import java.util.List;

import io.reactivex.Observable;

public interface PurchaseModel
{
    Observable<List<SkuDetails>> loadSkuInAppList(List<String> productIds);
    void purchase(PurchaseType purchaseType);
}
