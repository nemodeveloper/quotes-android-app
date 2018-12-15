package ru.nemodev.project.quotes.app;

import android.app.Application;

public class QuoteApp extends Application
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