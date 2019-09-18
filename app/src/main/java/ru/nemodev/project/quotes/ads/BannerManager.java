package ru.nemodev.project.quotes.ads;

import android.content.Context;

import com.google.android.gms.ads.MobileAds;

import java.util.List;

import ru.nemodev.core.utils.AndroidUtils;
import ru.nemodev.project.quotes.R;


public class BannerManager
{
    private final List<AdsBanner> adsBannerList;

    public BannerManager(Context context, List<AdsBanner> adsBannerList)
    {
        MobileAds.initialize(context, AndroidUtils.getString(R.string.ads_app_id));

        this.adsBannerList = adsBannerList;
        for (AdsBanner adsBanner : adsBannerList)
        {
            adsBanner.show();
        }
    }

    public void hideAds()
    {
        for (AdsBanner adsBanner : adsBannerList)
        {
            adsBanner.hide();
        }
    }
}
