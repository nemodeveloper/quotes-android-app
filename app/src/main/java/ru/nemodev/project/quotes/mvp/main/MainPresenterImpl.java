package ru.nemodev.project.quotes.mvp.main;

import android.app.Activity;
import android.content.Intent;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.adb.BannerManager;
import ru.nemodev.project.quotes.app.QuoteApp;
import ru.nemodev.project.quotes.mvp.purchase.PurchaseModel;
import ru.nemodev.project.quotes.mvp.purchase.PurchaseModelImpl;
import ru.nemodev.project.quotes.mvp.purchase.PurchaseType;
import ru.nemodev.project.quotes.utils.AndroidUtils;

public class MainPresenterImpl implements MainContract.MainPresenter, BillingProcessor.IBillingHandler
{
    private final Activity activity;
    private final BillingProcessor billingProcessor;
    private final PurchaseModel purchaseModel;

    private BannerManager bannerManager;

    public MainPresenterImpl(Activity activity)
    {
        this.activity = activity;

        this.billingProcessor = BillingProcessor.newBillingProcessor(
                QuoteApp.getInstance(),
                AndroidUtils.getString(R.string.APP_GOOGLE_RSA_PUBLIC_KEY),
                this);

        this.billingProcessor.initialize();
        this.purchaseModel = new PurchaseModelImpl(activity, billingProcessor);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        billingProcessor.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy()
    {
        billingProcessor.release();
    }

    @Override
    public PurchaseModel getPurchaseModel()
    {
        return purchaseModel;
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details)
    {
        if (PurchaseType.QUOTE_ADB.getSkuName().equals(productId))
        {
            bannerManager.disableAdb();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() { }

    @Override
    public void onBillingError(int errorCode, Throwable error) { }

    @Override
    public void onBillingInitialized()
    {
        bannerManager = new BannerManager(activity, activity.findViewById(R.id.adView),
                billingProcessor.isPurchased(PurchaseType.QUOTE_ADB.getSkuName()));
    }
}
