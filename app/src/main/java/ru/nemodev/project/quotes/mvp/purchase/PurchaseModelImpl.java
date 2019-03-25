package ru.nemodev.project.quotes.mvp.purchase;

import android.app.Activity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class PurchaseModelImpl implements PurchaseModel
{
    private final Activity activity;
    private final BillingProcessor billingProcessor;

    public PurchaseModelImpl(Activity activity, BillingProcessor billingProcessor)
    {
        this.activity = activity;
        this.billingProcessor = billingProcessor;
    }

    @Override
    public Observable<List<SkuDetails>> loadSkuInAppList(List<String> productIds)
    {
        return Observable.fromCallable(() ->
                billingProcessor.getPurchaseListingDetails(new ArrayList<>(productIds)))
            .subscribeOn(Schedulers.io());
    }

    @Override
    public void purchase(PurchaseType purchaseType)
    {
        billingProcessor.purchase(activity, purchaseType.getSkuName());
    }
}
