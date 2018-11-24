package ru.nemodev.project.quotes.app;

import android.support.multidex.MultiDexApplication;

public class QuoteApp extends MultiDexApplication
{
    private static QuoteApp instance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    public static QuoteApp getInstance()
    {
        return instance;
    }
}