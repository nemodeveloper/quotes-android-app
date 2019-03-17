package ru.nemodev.project.quotes.utils;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

public final class LogUtils
{
    private LogUtils() { }

    public static void logWithReport(String tag, String message, Throwable exception)
    {
        Log.e(tag, message, exception);
        Crashlytics.logException(exception);
    }
}
