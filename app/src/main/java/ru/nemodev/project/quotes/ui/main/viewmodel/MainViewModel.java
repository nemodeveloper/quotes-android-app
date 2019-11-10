package ru.nemodev.project.quotes.ui.main.viewmodel;

import android.app.Activity;
import android.net.NetworkInfo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;

import java.util.Arrays;

import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ads.AdsBanner;
import ru.nemodev.project.quotes.ads.BannerManager;
import ru.nemodev.project.quotes.ads.FullscreenBanner;
import ru.nemodev.project.quotes.ads.SimpleBanner;
import ru.nemodev.project.quotes.app.AndroidApplication;
import ru.nemodev.project.quotes.entity.purchase.Purchase;
import ru.nemodev.project.quotes.entity.purchase.PurchaseType;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class MainViewModel extends ViewModel implements
        AppUpdaterUtils.UpdateListener, AdsBanner.OnAdsListener {

    public final MutableLiveData<Boolean> updateAppEvent;
    public final MutableLiveData<Boolean> buyAdsEvent;

    public final MutableLiveData<NetworkInfo.State> networkState;
    private final Disposable internetEventsDisposable;

    private Activity activity;
    private BannerManager bannerManager;

    public MainViewModel() {
        updateAppEvent = new MutableLiveData<>();
        new AppUpdaterUtils(AndroidApplication.getInstance())
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .withListener(this).start();

        buyAdsEvent = new MutableLiveData<>();

        networkState = new MutableLiveData<>();
        internetEventsDisposable = NetworkUtils.getNetworkObservable()
                .subscribe(connectivity -> networkState.postValue(connectivity.state()));
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void onPurchase(Purchase purchase) {
        if (PurchaseType.QUOTE_ADB.getProductId().equals(purchase.getPurchaseType().getProductId())
                && bannerManager != null) {
            bannerManager.hideAds();
        }
    }

    public void onAdsBuy(Boolean isBuy) {
        if (!isBuy) {
            SimpleBanner simpleBanner = new SimpleBanner(activity.findViewById(R.id.adView));
            FullscreenBanner fullscreenBanner =
                    new FullscreenBanner(activity, this,
                            AndroidUtils.getInteger(R.integer.ads_fullscreen_banner_show_period));

            bannerManager = new BannerManager(activity, Arrays.asList(simpleBanner, fullscreenBanner));
        }
    }

    @Override
    public void onSuccess(Update update, Boolean isUpdateAvailable) {
        if (isUpdateAvailable) {
            updateAppEvent.postValue(true);
        }
    }

    @Override
    public void onFailed(AppUpdaterError error) { }

    @Override
    public void onAdsClose() {
        buyAdsEvent.postValue(true);
    }

    @Override
    protected void onCleared() {
        internetEventsDisposable.dispose();
        super.onCleared();
    }
}
