package ru.nemodev.project.quotes.app;

import android.content.Context;
import android.content.SharedPreferences;


public class AppSetting
{
    private static final String APP_SETTINGS_KEY = AppSetting.class.getCanonicalName() + "_PREFERENCES";

    private final SharedPreferences sharedPreferences;

    public AppSetting(Context context)
    {
        this.sharedPreferences = context.getSharedPreferences(APP_SETTINGS_KEY, Context.MODE_PRIVATE);
    }

    public void removeValue(String key)
    {
        sharedPreferences.edit().remove(key).apply();
    }

    public void setBoolean(String key, boolean value)
    {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key)
    {
        return sharedPreferences.getBoolean(key, false);
    }

    public void setLong(String key, Long value)
    {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public Long getLong(String key)
    {
        return sharedPreferences.getLong(key, 0L);
    }
}
