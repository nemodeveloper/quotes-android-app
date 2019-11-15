package ru.nemodev.project.quotes.ui.purchase.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import java.util.concurrent.Executors;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.AndroidApplication;
import ru.nemodev.project.quotes.entity.purchase.Purchase;
import ru.nemodev.project.quotes.entity.purchase.PurchaseType;
import ru.nemodev.project.quotes.ui.purchase.PurchaseInteractor;
import ru.nemodev.project.quotes.ui.purchase.PurchaseInteractorImpl;
import ru.nemodev.project.quotes.ui.purchase.source.PurchaseListDataSource;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;
import ru.nemodev.project.quotes.widget.QuoteWidgetProvider;


public class PurchaseViewModel extends ViewModel implements BillingProcessor.IBillingHandler {

    private final BillingProcessor billingProcessor;
    private final PurchaseInteractor purchaseInteractor;

    private Activity activity;

    public final MutableLiveData<Purchase> onPurchaseEvent;
    public final MutableLiveData<Boolean> onAdsByEvent;
    public MutableLiveData<PagedList<Purchase>> purchaseList;

    public PurchaseViewModel() {
        onPurchaseEvent = new MutableLiveData<>();
        onAdsByEvent = new MutableLiveData<>();

        purchaseList = new MutableLiveData<>();

        this.billingProcessor = BillingProcessor.newBillingProcessor(
                AndroidApplication.getInstance(),
                AndroidUtils.getString(R.string.APP_GOOGLE_RSA_PUBLIC_KEY),
                this);

        this.billingProcessor.initialize();
        this.purchaseInteractor = new PurchaseInteractorImpl(activity, billingProcessor);

        // TODO тут подумать лучше перейти на List с PagedList
        purchaseList.postValue(new PagedList.Builder<>(
                new PurchaseListDataSource(purchaseInteractor),
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(10)
                        .setPrefetchDistance(5)
                        .build())
                .setFetchExecutor(Executors.newCachedThreadPool())
                .setNotifyExecutor(command -> new Handler(Looper.getMainLooper()).post(command))
                .build());
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        billingProcessor.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        if (PurchaseType.QUOTE_WIDGET.getProductId().equals(productId)) {
            AndroidApplication.getInstance().getAppSetting().setBoolean(QuoteWidgetProvider.IS_PURCHASE_QUOTE_WIDGET_KEY, true);
        }

        purchaseInteractor.loadPurchase(productId).subscribe(new Observer<Purchase>() {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(Purchase purchase) {
                MetricUtils.purchaseEvent(purchase);
                onPurchaseEvent.postValue(purchase);
                purchaseList.postValue(new PagedList.Builder<>(
                        new PurchaseListDataSource(purchaseInteractor),
                        new PagedList.Config.Builder()
                                .setEnablePlaceholders(false)
                                .setPageSize(10)
                                .setPrefetchDistance(5)
                                .build())
                        .build());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() { }
        });
    }

    @Override
    public void onPurchaseHistoryRestored() { }

    @Override
    public void onBillingError(int errorCode, Throwable error) { }

    @Override
    public void onBillingInitialized() {
        purchaseInteractor.loadOwnedPurchaseList();

        if (!purchaseInteractor.isPurchase(PurchaseType.QUOTE_ADB))
        {
            onAdsByEvent.postValue(false);
        }

        AndroidApplication.getInstance().getAppSetting().setBoolean(
                QuoteWidgetProvider.IS_PURCHASE_QUOTE_WIDGET_KEY,
                purchaseInteractor.isPurchase(PurchaseType.QUOTE_WIDGET));
    }

    public void purchase(Purchase purchase) {
        purchase(purchase.getPurchaseType());
    }

    public void purchase(PurchaseType purchaseType) {
        purchaseInteractor.purchase(purchaseType);
    }

}
