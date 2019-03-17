package ru.nemodev.project.quotes.adb;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.utils.AndroidUtils;

public class BannerManager
{
    private static final String LOG_TAG = BannerManager.class.getSimpleName();

    private final InterstitialAd fullScreenBanner;
    private final AdView simpleBanner;

    public BannerManager(Context context, AdView simpleBanner)
    {
        MobileAds.initialize(context, AndroidUtils.getTextById(R.string.ads_app_id));

        this.simpleBanner = simpleBanner;
        initSimpleBanner();

        this.fullScreenBanner = new InterstitialAd(context);
        initFullScreenBanner();
    }

    public boolean showFullScreenBanner()
    {
        if (fullScreenBanner.isLoaded())
        {
            fullScreenBanner.show();
            return true;
        }
        else
        {
            loadNewFullscreenBanner();
            return false;
        }
    }

    private void initSimpleBanner()
    {
        simpleBanner.loadAd(new AdRequest.Builder().build());
    }

    private void initFullScreenBanner()
    {
        try
        {
            fullScreenBanner.setAdUnitId(AndroidUtils.getTextById(R.string.ads_fullscreen_banner_id));
            fullScreenBanner.loadAd(new AdRequest.Builder().build());
            fullScreenBanner.setAdListener(new AdListener() {
                @Override
                public void onAdClosed()
                {
                    loadNewFullscreenBanner();
                }
            });

            Observable.interval(2, TimeUnit.MINUTES)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>()
                    {
                        @Override
                        public void onSubscribe(Disposable d) { }

                        @Override
                        public void onNext(Long aLong)
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

                        @Override
                        public void onError(Throwable e)
                        {
                            Log.e(LOG_TAG, "Ошибка показа полноэкранного баннера", e);
                        }

                        @Override
                        public void onComplete() { }
                    });
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "Ошибка инициализации полноэкранного баннера", e);
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
