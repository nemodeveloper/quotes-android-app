package ru.nemodev.project.quotes.mvp.main;

import android.app.Activity;
import android.content.Intent;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;

import java.util.Arrays;

import ru.nemodev.core.app.AndroidApplication;
import ru.nemodev.core.utils.AndroidUtils;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ads.AdsBanner;
import ru.nemodev.project.quotes.ads.BannerManager;
import ru.nemodev.project.quotes.ads.FullscreenBanner;
import ru.nemodev.project.quotes.ads.SimpleBanner;
import ru.nemodev.project.quotes.mvp.purchase.BillingEventListener;
import ru.nemodev.project.quotes.mvp.purchase.PurchaseInteractor;
import ru.nemodev.project.quotes.mvp.purchase.PurchaseInteractorImpl;
import ru.nemodev.project.quotes.mvp.purchase.PurchaseType;
import ru.nemodev.project.quotes.utils.MetricUtils;
import ru.nemodev.project.quotes.widget.QuoteWidgetProvider;


public class MainPresenterImpl implements MainContract.MainPresenter,
        BillingProcessor.IBillingHandler, AppUpdaterUtils.UpdateListener, AdsBanner.OnAdsListener
{
    private final MainContract.MainView mainView;
    private final Activity activity;
    private final BillingProcessor billingProcessor;
    private final PurchaseInteractor purchaseInteractor;

    private BillingEventListener billingEventListener;
    private BannerManager bannerManager;
    private final AppUpdaterUtils appUpdater;

    public MainPresenterImpl(Activity activity, MainContract.MainView mainView)
    {
        this.activity = activity;
        this.mainView = mainView;

        this.billingProcessor = BillingProcessor.newBillingProcessor(
                AndroidApplication.getInstance(),
                AndroidUtils.getString(R.string.APP_GOOGLE_RSA_PUBLIC_KEY),
                this);

        this.billingProcessor.initialize();
        this.purchaseInteractor = new PurchaseInteractorImpl(activity, billingProcessor);

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
    public PurchaseInteractor getPurchaseInteractor()
    {
        return purchaseInteractor;
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

        if (PurchaseType.QUOTE_ADB.getProductId().equals(productId) && bannerManager != null)
        {
            bannerManager.hideAds();
        }
        else if (PurchaseType.QUOTE_WIDGET.getProductId().equals(productId))
        {
            AndroidApplication.getInstance().getAppSetting().setBoolean(QuoteWidgetProvider.IS_PURCHASE_QUOTE_WIDGET_KEY, true);
        }

        purchaseInteractor.loadPurchase(productId).subscribe(MetricUtils::purchaseEvent);
    }

    @Override
    public void onPurchaseHistoryRestored() { }

    @Override
    public void onBillingError(int errorCode, Throwable error) { }

    @Override
    public void onBillingInitialized()
    {
        purchaseInteractor.loadOwnedPurchaseList();

        if (!purchaseInteractor.isPurchase(PurchaseType.QUOTE_ADB))
        {
            SimpleBanner simpleBanner = new SimpleBanner(activity.findViewById(R.id.adView));
            FullscreenBanner fullscreenBanner =
                    new FullscreenBanner(activity, this,
                            AndroidUtils.getInteger(R.integer.ads_fullscreen_banner_show_period));

            bannerManager = new BannerManager(activity, Arrays.asList(simpleBanner, fullscreenBanner));
        }

        AndroidApplication.getInstance().getAppSetting().setBoolean(
                QuoteWidgetProvider.IS_PURCHASE_QUOTE_WIDGET_KEY,
                purchaseInteractor.isPurchase(PurchaseType.QUOTE_WIDGET));

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
    public void onClose()
    {
        mainView.showDisableAdsDialog();
    }
}
