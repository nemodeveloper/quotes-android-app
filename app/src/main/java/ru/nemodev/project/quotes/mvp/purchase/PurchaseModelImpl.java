package ru.nemodev.project.quotes.mvp.purchase;

import android.app.Activity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.Purchase;

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
    public Observable<List<Purchase>> loadPurchaseInAppList(List<String> productIds)
    {
        return Observable.fromCallable(() ->
                billingProcessor.getPurchaseListingDetails(new ArrayList<>(productIds)))
            .map(this::toPurchase)
            .subscribeOn(Schedulers.io());
    }

    @Override
    public void purchase(PurchaseType purchaseType)
    {
        billingProcessor.purchase(activity, purchaseType.getProductId());
    }

    @Override
    public boolean isPurchase(PurchaseType purchaseType)
    {
        return billingProcessor.isPurchased(purchaseType.getProductId());
    }

    private List<Purchase> toPurchase(List<SkuDetails> skuDetails)
    {
        if (CollectionUtils.isEmpty(skuDetails))
            return Collections.emptyList();

        List<Purchase> purchaseList = new ArrayList<>(skuDetails.size());
        for (SkuDetails skuDetail : skuDetails)
        {
            purchaseList.add(new Purchase(skuDetail,
                    isPurchase(PurchaseType.getByProductId(skuDetail.productId))));
        }

        return purchaseList;
    }
}
