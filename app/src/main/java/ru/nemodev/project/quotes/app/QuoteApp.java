package ru.nemodev.project.quotes.app;

import android.app.Application;

public class QuoteApp extends Application
{
    private static QuoteApp instance;

    private AppSetting appSetting;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;

        appSetting = new AppSetting(getApplicationContext());
    }

    public static QuoteApp getInstance()
    {
        return instance;
    }

    public AppSetting getAppSetting()
    {
        return appSetting;
    }
}