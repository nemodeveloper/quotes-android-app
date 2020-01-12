package ru.nemodev.project.quotes.service.purchase;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.AndroidApplication;
import ru.nemodev.project.quotes.app.config.AdsConfig;
import ru.nemodev.project.quotes.app.config.FirebaseConfig;
import ru.nemodev.project.quotes.entity.purchase.PurchaseItem;
import ru.nemodev.project.quotes.entity.purchase.PurchaseType;
import ru.nemodev.project.quotes.ui.purchase.source.PurchaseListDataSource;
import ru.nemodev.project.quotes.utils.AnalyticUtils;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class PurchaseService implements PurchasesUpdatedListener {

    private Activity activity;
    private final Map<String, SkuDetails> skuDetailsMap;
    private final BillingClient billingClient;

    private final MutableLiveData<Purchase> onPurchaseEvent;
    private final MutableLiveData<PagedList<PurchaseItem>> purchaseList;

    public PurchaseService(Activity activity) {
        this.activity = activity;

        onPurchaseEvent = new MutableLiveData<>();
        purchaseList = new MutableLiveData<>();

        skuDetailsMap = new ConcurrentHashMap<>();
        billingClient = BillingClient.newBuilder(AndroidApplication.getInstance())
                .enablePendingPurchases()
                .setListener(this)
                .build();

        refreshPurchase();
    }

    public LiveData<Purchase> getOnPurchaseEvent() {
        return onPurchaseEvent;
    }

    public LiveData<PagedList<PurchaseItem>> getPurchaseList() {
        return purchaseList;
    }

    public void buy(PurchaseItem purchaseItem) {
        buy(purchaseItem.getSkuDetails());
    }

    public void buy(PurchaseType purchaseType) {
        buy(skuDetailsMap.get(purchaseType.getSku()));
    }

    public void buy(SkuDetails skuDetails) {
        if (skuDetails == null) {
            return;
        }

        billingClient.launchBillingFlow(activity,
                BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetails)
                        .build());
    }

    public boolean isPurchase(PurchaseType purchaseType) {
        for (Purchase purchase : getClientPurchaseList()) {
            if (purchaseType.getSku().equals(purchase.getSku())) {
                return true;
            }
        }

        return false;
    }

    public void refreshPurchase() {
        billingClient.startConnection(new BillingClientStateListener() {

            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    querySkuDetails();
                }
            }

            @Override
            public void onBillingServiceDisconnected() { }
        });
    }

    private void querySkuDetails() {
        SkuDetailsParams.Builder skuDetailsParamsBuilder = SkuDetailsParams.newBuilder()
                .setSkusList(AndroidUtils.getStringList(R.array.inapp_products))
                .setType(BillingClient.SkuType.INAPP);

        billingClient.querySkuDetailsAsync(skuDetailsParamsBuilder.build(), (billingResult, skuDetailsList) -> {
            List<PurchaseItem> purchaseItems = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(skuDetailsList)) {
                Set<String> purchaseSkuList = new HashSet<>();
                for (Purchase purchase : getClientPurchaseList()) {
                    purchaseSkuList.add(purchase.getSku());
                }

                for (SkuDetails skuDetails : skuDetailsList) {
                    skuDetailsMap.put(skuDetails.getSku(), skuDetails);
                    purchaseItems.add(new PurchaseItem(skuDetails, purchaseSkuList.contains(skuDetails.getSku())));
                }
            }

            if (!FirebaseConfig.getBoolean(AdsConfig.NEED_SHOW_ADS)) {
                Iterator<PurchaseItem> purchaseItemIterator = purchaseItems.iterator();
                while (purchaseItemIterator.hasNext()) {
                    if (PurchaseType.QUOTE_ADS.equals(purchaseItemIterator.next().getPurchaseType())) {
                        purchaseItemIterator.remove();
                        break;
                    }
                }
            }

            purchaseList.postValue(new PagedList.Builder<>(
                    new PurchaseListDataSource(purchaseItems),
                    new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setPageSize(10)
                            .setPrefetchDistance(5)
                            .build())
                    .setFetchExecutor(Executors.newCachedThreadPool())
                    .setNotifyExecutor(command -> new Handler(Looper.getMainLooper()).post(command))
                    .build());
        });
    }

    private List<Purchase> getClientPurchaseList() {
        Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
        if (purchasesResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && CollectionUtils.isNotEmpty(purchasesResult.getPurchasesList())) {
            return purchasesResult.getPurchasesList();
        }

        return Collections.emptyList();
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (CollectionUtils.isNotEmpty(purchases)) {
            refreshPurchase();
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        }
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            PurchaseItem purchaseItem = getPurchaseItem(purchase);
            if (purchaseItem != null) {
                AnalyticUtils.purchaseEvent(purchaseItem);
            }

            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {

                });
            }

            onPurchaseEvent.postValue(purchase);
        }
    }

    private PurchaseItem getPurchaseItem(Purchase purchase) {
        if (purchaseList.getValue() == null) {
            return null;
        }

        for (PurchaseItem purchaseItem : purchaseList.getValue().snapshot()) {
            if (purchase.getSku().equals(purchaseItem.getSku())) {
                return purchaseItem;
            }
        }

        return null;
    }
}
