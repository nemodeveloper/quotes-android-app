package ru.nemodev.project.quotes.ui.main.viewmodel.ads;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.billingclient.api.Purchase;

import java.util.ArrayList;
import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ads.AdsBanner;
import ru.nemodev.project.quotes.ads.BannerManager;
import ru.nemodev.project.quotes.ads.FullscreenBanner;
import ru.nemodev.project.quotes.ads.SimpleBanner;
import ru.nemodev.project.quotes.app.config.AdsConfig;
import ru.nemodev.project.quotes.entity.purchase.PurchaseType;
import ru.nemodev.project.quotes.utils.LogUtils;


public class AdsViewModel extends ViewModel implements AdsBanner.OnAdsListener {

    private static final String LOG_TAG = AdsViewModel.class.getSimpleName();

    private final Activity activity;
    private BannerManager bannerManager;
    private final MutableLiveData<Boolean> onFullscreenBannerCloseEvent;

    public AdsViewModel(Activity activity) {
        this.activity = activity;
        this.onFullscreenBannerCloseEvent = new MutableLiveData<>();
    }

    @Override
    public void onAdsClose() {
        onFullscreenBannerCloseEvent.postValue(true);
    }

    public LiveData<Boolean> getOnFullscreenBannerCloseEvent() {
        return onFullscreenBannerCloseEvent;
    }

    public void onBuyEvent(Purchase purchase) {
        if (PurchaseType.QUOTE_ADS.getSku().equals(purchase.getSku())) {
            onAdsBuyEvent(true);
        }
    }

    public void onAdsBuyEvent(boolean isBuy) {
        if (isBuy) {
            if (bannerManager != null) {
                bannerManager.hideAds();
            }
        }
        else {
            if (AdsConfig.isShowSomeAds() && bannerManager == null) {
                try {
                    List<AdsBanner> banners = new ArrayList<>();
                    if (AdsConfig.isShowSimpleBanner()) {
                        banners.add(new SimpleBanner(activity.findViewById(R.id.adView)));
                    }
                    if (AdsConfig.isShowFullscreenBanner()) {
                        banners.add(new FullscreenBanner(activity, this, AdsConfig.getFullscreenBannerShowPeriodSec()));
                    }

                    bannerManager = new BannerManager(activity, banners);
                }
                catch (Exception e) {
                    LogUtils.error(LOG_TAG, "Ошибка показа рекламы!", e);
                }
            }
        }
    }
}
