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
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.LogUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;

public class BannerManager
{
    private static final String LOG_TAG = BannerManager.class.getSimpleName();
    private final Context context;
    private final AdView simpleBanner;

    private InterstitialAd fullScreenBanner;
    private Disposable fullScreenBannerDisposable;

    public BannerManager(Context context, AdView simpleBanner, boolean isPurchaseAdb)
    {
        this.context = context;
        this.simpleBanner = simpleBanner;

        if (!isPurchaseAdb)
        {
            MobileAds.initialize(context, AndroidUtils.getString(R.string.ads_app_id));
            initSimpleBanner();
            initFullScreenBanner();
        }
    }

    public void disableAdb()
    {
        simpleBanner.setVisibility(View.GONE);
        if (fullScreenBannerDisposable != null && !fullScreenBannerDisposable.isDisposed())
        {
            fullScreenBannerDisposable.dispose();
        }
    }

    private void initSimpleBanner()
    {
        simpleBanner.setVisibility(View.VISIBLE);
        simpleBanner.loadAd(new AdRequest.Builder().build());
    }

    private void initFullScreenBanner()
    {
        try
        {
            fullScreenBanner = new InterstitialAd(context);
            fullScreenBanner.setAdUnitId(AndroidUtils.getString(R.string.ads_fullscreen_banner_id));
            fullScreenBanner.loadAd(new AdRequest.Builder().build());
            fullScreenBanner.setAdListener(new AdListener() {
                @Override
                public void onAdClosed()
                {
                    loadNewFullscreenBanner();
                }
            });

            fullScreenBannerDisposable = Observable.interval(3, TimeUnit.MINUTES)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> showFullScreenBanner(),
                            throwable -> LogUtils.logWithReport(LOG_TAG, "Ошибка показа полноэкранного баннера", throwable));
        }
        catch (Exception e)
        {
            LogUtils.logWithReport(LOG_TAG, "Ошибка инициализации полноэкранного баннера", e);
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
            fullScreenBanner.loadAd(new AdRequest.Builder().build());
        }
    }
}
