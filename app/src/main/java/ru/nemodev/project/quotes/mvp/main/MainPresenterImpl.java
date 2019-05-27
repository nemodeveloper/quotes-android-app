package ru.nemodev.project.quotes.mvp.main;

import android.app.Activity;
import android.content.Intent;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.adb.BannerManager;
import ru.nemodev.project.quotes.app.AppSetting;
import ru.nemodev.project.quotes.app.QuoteApp;
import ru.nemodev.project.quotes.mvp.purchase.BillingEventListener;
import ru.nemodev.project.quotes.mvp.purchase.PurchaseModel;
import ru.nemodev.project.quotes.mvp.purchase.PurchaseModelImpl;
import ru.nemodev.project.quotes.mvp.purchase.PurchaseType;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;

public class MainPresenterImpl implements MainContract.MainPresenter,
        BillingProcessor.IBillingHandler, AppUpdaterUtils.UpdateListener, BannerManager.OnAdListener
{
    private final MainContract.MainView mainView;
    private final Activity activity;
    private final BillingProcessor billingProcessor;
    private final PurchaseModel purchaseModel;

    private BillingEventListener billingEventListener;
    private BannerManager bannerManager;
    private final AppUpdaterUtils appUpdater;

    public MainPresenterImpl(Activity activity, MainContract.MainView mainView)
    {
        this.activity = activity;
        this.mainView = mainView;

        this.billingProcessor = BillingProcessor.newBillingProcessor(
                QuoteApp.getInstance(),
                AndroidUtils.getString(R.string.APP_GOOGLE_RSA_PUBLIC_KEY),
                this);

        this.billingProcessor.initialize();
        this.purchaseModel = new PurchaseModelImpl(activity, billingProcessor);

        appUpdater = new AppUpdaterUtils(activity)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .withListener(this);
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
    public void setBillingEventListener(BillingEventListener billingEventListener)
    {
        this.billingEventListener = billingEventListener;
    }

    @Override
    public void checkAppUpdate()
    {
        appUpdater.start();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details)
    {
        if (billingEventListener != null)
        {
            billingEventListener.onPurchase(productId);
        }

        if (PurchaseType.QUOTE_ADB.getProductId().equals(productId))
        {
            bannerManager.disableAdb();
        }
        else if (PurchaseType.QUOTE_WIDGET.getProductId().equals(productId))
        {
            QuoteApp.getInstance().getAppSetting().setBoolean(AppSetting.IS_PURCHASE_QUOTE_WIDGET_KEY, true);
        }

        purchaseModel.loadPurchase(productId).subscribe(MetricUtils::purchaseEvent);
    }

    @Override
    public void onPurchaseHistoryRestored() { }

    @Override
    public void onBillingError(int errorCode, Throwable error) { }

    @Override
    public void onBillingInitialized()
    {
        bannerManager = new BannerManager(activity, activity.findViewById(R.id.adView), this,
                false);

        QuoteApp.getInstance().getAppSetting().setBoolean(
                AppSetting.IS_PURCHASE_QUOTE_WIDGET_KEY,
                purchaseModel.isPurchase(PurchaseType.QUOTE_WIDGET));

        purchaseModel.loadOwnedPurchaseList();
    }

    @Override
    public void onSuccess(Update update, Boolean isUpdateAvailable)
    {
        if (isUpdateAvailable)
            mainView.showUpdateDialog();
    }

    @Override
    public void onFailed(AppUpdaterError error) { }

    @Override
    public void onAdClose()
    {
        mainView.showDisableAdbDialog();
    }
}
