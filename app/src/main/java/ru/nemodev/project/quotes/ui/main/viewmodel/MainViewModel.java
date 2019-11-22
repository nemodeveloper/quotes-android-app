package ru.nemodev.project.quotes.ui.main.viewmodel;

import android.app.Activity;
import android.content.IntentSender;
import android.net.NetworkInfo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.billingclient.api.Purchase;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.util.Arrays;

import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ads.AdsBanner;
import ru.nemodev.project.quotes.ads.BannerManager;
import ru.nemodev.project.quotes.ads.FullscreenBanner;
import ru.nemodev.project.quotes.ads.SimpleBanner;
import ru.nemodev.project.quotes.app.AndroidApplication;
import ru.nemodev.project.quotes.entity.purchase.PurchaseType;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;
import ru.nemodev.project.quotes.widget.QuoteWidgetProvider;


public class MainViewModel extends ViewModel implements AdsBanner.OnAdsListener {

    public final MutableLiveData<InstallState> updateAppEvent;
    public final MutableLiveData<Boolean> buyAdsRequest;

    public final MutableLiveData<NetworkInfo.State> networkState;
    private final Disposable internetEventsDisposable;

    private AppUpdateManager appUpdateManager;
    private Activity activity;
    private BannerManager bannerManager;

    public MainViewModel(Activity activity) {
        this.activity = activity;
        updateAppEvent = new MutableLiveData<>();
        buyAdsRequest = new MutableLiveData<>();
        networkState = new MutableLiveData<>();
        internetEventsDisposable = NetworkUtils.getNetworkObservable()
                .subscribe(connectivity -> networkState.postValue(connectivity.state()));

        appUpdateManager = AppUpdateManagerFactory.create(activity);
        appUpdateManager.getAppUpdateInfo()
                .addOnSuccessListener(appUpdateInfo -> {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo,
                                    AppUpdateType.FLEXIBLE,
                                    activity,
                                    1077);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo,
                                    AppUpdateType.IMMEDIATE,
                                    activity,
                                    1077);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(e -> { });
        appUpdateManager.registerListener(state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                updateAppEvent.postValue(state);
            }
        });
    }

    public void onPurchase(Purchase purchase) {
        if (PurchaseType.QUOTE_ADB.getSku().equals(purchase.getSku())) {
            onAdsBuy(true);
        }
        else if (PurchaseType.QUOTE_WIDGET.getSku().equals(purchase.getSku())) {
            AndroidApplication.getInstance().getAppSetting().setBoolean(QuoteWidgetProvider.IS_PURCHASE_QUOTE_WIDGET_KEY, true);
        }
    }

    public void onAdsBuy(Boolean isBuy) {
        if (isBuy) {
            if (bannerManager != null) {
                bannerManager.hideAds();
            }
        }
        else {
            SimpleBanner simpleBanner = new SimpleBanner(activity.findViewById(R.id.adView));
            FullscreenBanner fullscreenBanner =
                    new FullscreenBanner(activity, this,
                            AndroidUtils.getInteger(R.integer.ads_fullscreen_banner_show_period));

            bannerManager = new BannerManager(activity, Arrays.asList(simpleBanner, fullscreenBanner));
        }

    }

    @Override
    public void onAdsClose() {
        buyAdsRequest.postValue(true);
    }

    @Override
    protected void onCleared() {
        internetEventsDisposable.dispose();
        super.onCleared();
    }

    public void appUpdate() {
        appUpdateManager.completeUpdate();
    }
}
