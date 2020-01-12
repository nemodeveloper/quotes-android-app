package ru.nemodev.project.quotes.app.config;

public final class AdsConfig {

    private static final String NEED_SHOW_SIMPLE_BANNER_ADS = "show_simple_banner_ads";

    private static final String NEED_SHOW_FULLSCREEN_BANNER_ADS = "show_fullscreen_banner_ads";
    private static final String FULLSCREEN_BANNER_SHOW_PERIOD_SEC = "fullscreen_banner_show_period_sec";

    public static boolean isShowSomeAds() {
        return isShowSimpleBanner() || isShowFullscreenBanner();
    }

    public static boolean isShowSimpleBanner() {
        return FirebaseConfig.getBoolean(AdsConfig.NEED_SHOW_SIMPLE_BANNER_ADS);
    }

    public static boolean isShowFullscreenBanner() {
        return FirebaseConfig.getBoolean(AdsConfig.NEED_SHOW_FULLSCREEN_BANNER_ADS);
    }

    public static Integer getFullscreenBannerShowPeriodSec() {
        return FirebaseConfig.getInteger(AdsConfig.FULLSCREEN_BANNER_SHOW_PERIOD_SEC);
    }
}
