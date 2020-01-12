package ru.nemodev.project.quotes.ads;

import android.content.Context;

import com.google.android.gms.ads.MobileAds;

import java.util.Collections;
import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class BannerManager {
    private final List<AdsBanner> adsBannerList;

    public BannerManager(Context context, List<AdsBanner> adsBannerList) {
        MobileAds.initialize(context, AndroidUtils.getString(R.string.ads_app_id));

        this.adsBannerList = Collections.unmodifiableList(adsBannerList);
        for (AdsBanner adsBanner : adsBannerList) {
            adsBanner.show();
        }
    }

    public void hideAds() {
        for (AdsBanner adsBanner : adsBannerList) {
            adsBanner.hide();
        }
    }
}
