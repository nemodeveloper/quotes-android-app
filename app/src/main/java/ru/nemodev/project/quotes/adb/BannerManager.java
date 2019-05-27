package ru.nemodev.project.quotes.adb;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.BuildConfig;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.LogUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;

public class BannerManager
{
    public interface OnAdListener
    {
        void onAdClose();
    }

    private static final String LOG_TAG = BannerManager.class.getSimpleName();
    private static final int SHOW_FULL_SCREEN_BANNER_PERIOD_SEC = 2;

    private final Context context;
    private final AdView simpleBanner;
    private final OnAdListener onAdListener;

    private InterstitialAd fullScreenBanner;
    private Disposable fullScreenBannerDisposable;

    public BannerManager(Context context, AdView simpleBanner, OnAdListener onAdListener, boolean isPurchaseAdb)
    {
        this.context = context;
        this.simpleBanner = simpleBanner;
        this.onAdListener = onAdListener;

        if (!isPurchaseAdb)
        {
            MobileAds.initialize(context, AndroidUtils.getString(R.string.ads_app_id));
            initSimpleBanner();
            initFullScreenBanner();
        }
    }

    public void disableAdb()
    {
        try
        {
            simpleBanner.setVisibility(View.GONE);
            if (fullScreenBannerDisposable != null && !fullScreenBannerDisposable.isDisposed())
            {
                fullScreenBannerDisposable.dispose();
            }
        }
        catch (Exception e)
        {
            LogUtils.logWithReport(LOG_TAG, "Ошибка при отключении рекламы!", e);
        }
    }

    private AdRequest buildAdRequest()
    {
        AdRequest.Builder builder = new AdRequest.Builder();
        if (BuildConfig.DEBUG)
        {
            builder.addTestDevice(AndroidUtils.getString(R.string.device_id_test));
        }

        return builder.build();
    }

    private void initSimpleBanner()
    {
        try
        {
            simpleBanner.setVisibility(View.VISIBLE);
            simpleBanner.loadAd(buildAdRequest());
        }
        catch (Exception e)
        {
            LogUtils.logWithReport(LOG_TAG, "Ошибка инициализации SimpleBanner!", e);
        }
    }

    private void initFullScreenBanner()
    {
        try
        {
            fullScreenBanner = new InterstitialAd(context);
            fullScreenBanner.setAdUnitId(BuildConfig.DEBUG
                    ? AndroidUtils.getString(R.string.ads_fullscreen_banner_id_test)
                    : AndroidUtils.getString(R.string.ads_fullscreen_banner_id));

            fullScreenBanner.setAdListener(new AdListener() {
                @Override
                public void onAdClosed()
                {
                    loadNewFullscreenBanner();
                    onAdListener.onAdClose();
                }
            });

            fullScreenBannerDisposable = Observable.interval(SHOW_FULL_SCREEN_BANNER_PERIOD_SEC, TimeUnit.MINUTES)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> showFullScreenBanner(),
                            throwable -> LogUtils.logWithReport(LOG_TAG, "Ошибка показа полноэкранного баннера", throwable));

            loadNewFullscreenBanner();
        }
        catch (Exception e)
        {
            LogUtils.logWithReport(LOG_TAG, "Ошибка инициализации полноэкранного баннера!", e);
        }
    }

    private void showFullScreenBanner()
    {
        if (fullScreenBanner.isLoaded())
        {
            MetricUtils.viewEvent(MetricUtils.ViewType.FULL_SCREEN_BANNER);
            fullScreenBanner.show();
        }
        else
        {
            loadNewFullscreenBanner();
        }
    }

    private void loadNewFullscreenBanner()
    {
        if (!fullScreenBanner.isLoaded())
        {
            fullScreenBanner.loadAd(buildAdRequest());
        }
    }
}
