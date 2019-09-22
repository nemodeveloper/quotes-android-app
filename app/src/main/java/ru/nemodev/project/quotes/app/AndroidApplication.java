package ru.nemodev.project.quotes.app;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


public class AndroidApplication extends android.app.Application
{
    private static AndroidApplication instance;

    private AppSetting appSetting;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;

        appSetting = new AppSetting(getApplicationContext());
        initFabricIO();
    }

    private void initFabricIO()
    {
        Fabric.with(this, new Crashlytics());
    }

    public static AndroidApplication getInstance()
    {
        return instance;
    }

    public AppSetting getAppSetting()
    {
        return appSetting;
    }
}