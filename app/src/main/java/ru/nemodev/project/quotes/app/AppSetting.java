package ru.nemodev.project.quotes.app;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSetting
{
    private static final String QUOTES_APP_SETTINGS_KEY = "ru.nemodev.project.quotes.PREFERENCES";
    public static final String IS_PURCHASE_QUOTE_WIDGET_KEY = "IS_PURCHASE_QUOTE_WIDGET";

    private final SharedPreferences sharedPreferences;

    public AppSetting(Context context)
    {
        this.sharedPreferences = context.getSharedPreferences(QUOTES_APP_SETTINGS_KEY, Context.MODE_PRIVATE);
    }

    public void setBoolean(String key, boolean value)
    {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key)
    {
        return sharedPreferences.getBoolean(key, false);
    }
}