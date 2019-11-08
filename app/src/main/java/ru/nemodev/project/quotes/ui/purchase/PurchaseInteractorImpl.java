package ru.nemodev.project.quotes.ui.purchase;

import android.app.Activity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.purchase.Purchase;
import ru.nemodev.project.quotes.entity.purchase.PurchaseType;
import ru.nemodev.project.quotes.utils.AndroidUtils;

public class PurchaseInteractorImpl implements PurchaseInteractor
{
    private final Activity activity;
    private final BillingProcessor billingProcessor;

    public PurchaseInteractorImpl(Activity activity, BillingProcessor billingProcessor)
    {
        this.activity = activity;
        this.billingProcessor = billingProcessor;
    }

    @Override
    public Observable<List<Purchase>> loadPurchaseInAppList()
    {
        return loadPurchaseInAppList(AndroidUtils.getStringList(R.array.inapp_products));
    }

    private Observable<List<Purchase>> loadPurchaseInAppList(List<String> purchaseIds)
    {
        return Observable.fromCallable(() ->
                billingProcessor.getPurchaseListingDetails(new ArrayList<>(purchaseIds)))
                .map(this::toPurchase)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Purchase> loadPurchase(String productId)
    {
        return loadPurchaseInAppList(Collections.singletonList(productId))
                .map(purchaseList -> purchaseList.get(0));
    }

    @Override
    public void loadOwnedPurchaseList()
    {
        billingProcessor.loadOwnedPurchasesFromGoogle();
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

        Collections.sort(purchaseList, (o1, o2) ->
        {
            if (!o1.isPurchase() || !o2.isPurchase())
            {
                return Boolean.compare(o1.isPurchase(), o2.isPurchase());
            }

            return o2.getPurchaseType().compareTo(o1.getPurchaseType());
        });

        return purchaseList;
    }
}
