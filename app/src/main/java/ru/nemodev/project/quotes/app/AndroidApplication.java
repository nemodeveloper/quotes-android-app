package ru.nemodev.project.quotes.app;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;
import ru.nemodev.project.quotes.app.config.FirebaseConfig;


public class AndroidApplication extends android.app.Application {
    private static AndroidApplication instance;

    private AppSetting appSetting;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        FirebaseConfig.getInstance();
        appSetting = new AppSetting(getApplicationContext());
        initCrashlytics();
    }

    private void initCrashlytics() {
        Fabric.with(this, new Crashlytics());
    }

    public static AndroidApplication getInstance() {
        return instance;
    }

    public AppSetting getAppSetting() {
        return appSetting;
    }

    public static FirebaseAnalytics getAnalytics() {
        return FirebaseAnalytics.getInstance(getInstance());
    }
}