package ru.nemodev.project.quotes.ads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.BuildConfig;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.LogUtils;


public class FullscreenBanner implements AdsBanner
{
    private static final String LOG_TAG = FullscreenBanner.class.getSimpleName();

    private final Context context;
    private final OnAdsListener onAdsListener;
    private final int showPeriodMinute;

    private PublisherInterstitialAd fullScreenBanner;
    private Disposable fullScreenBannerDisposable;

    public FullscreenBanner(Context context, OnAdsListener onAdsListener, int showPeriodMinute)
    {
        this.context = context;
        this.onAdsListener = onAdsListener;
        this.showPeriodMinute = showPeriodMinute;
    }

    @Override
    public void show()
    {
        if (fullScreenBanner != null)
            return;

        try
        {
            fullScreenBanner = new PublisherInterstitialAd(context);
            fullScreenBanner.setAdUnitId(BuildConfig.DEBUG
                    ? AndroidUtils.getString(R.string.ads_fullscreen_banner_id_test)
                    : AndroidUtils.getString(R.string.ads_fullscreen_banner_id));

            fullScreenBanner.setAdListener(new AdListener() {
                @Override
                public void onAdClosed()
                {
                    loadNewFullscreenBanner();
                    onAdsListener.onClose();
                }
            });

            fullScreenBannerDisposable = Observable.interval(showPeriodMinute, TimeUnit.MINUTES)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> showFullScreenBanner(),
                            throwable -> LogUtils.error(LOG_TAG, "Ошибка показа полноэкранного баннера", throwable));

            loadNewFullscreenBanner();
        }
        catch (Exception e)
        {
            LogUtils.error(LOG_TAG, "Ошибка инициализации полноэкранного баннера!", e);
        }
    }

    @Override
    public void hide()
    {
        try
        {
            if (fullScreenBannerDisposable != null && !fullScreenBannerDisposable.isDisposed())
            {
                fullScreenBannerDisposable.dispose();
            }
        }
        catch (Exception e)
        {
            LogUtils.error(LOG_TAG, "Ошибка при отключении полноэкранной рекламы!", e);
        }
    }

    private void showFullScreenBanner()
    {
        if (fullScreenBanner.isLoaded())
        {
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

    private PublisherAdRequest buildAdRequest()
    {
        PublisherAdRequest.Builder builder = new PublisherAdRequest.Builder();
        if (BuildConfig.DEBUG)
        {
            builder.addTestDevice(AndroidUtils.getString(R.string.device_id_test));
        }

        return builder.build();
    }
}
