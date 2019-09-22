package ru.nemodev.project.quotes.utils;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.AndroidApplication;


public final class AndroidUtils
{
    private static final String LOG_TAG = AndroidUtils.class.getSimpleName();

    private static final String DIALOG_SHARE_TYPE = "text/plain";

    private AndroidUtils() { }

    public static String getString(int resId)
    {
        return AndroidApplication.getInstance().getResources().getString(resId);
    }

    public static int getInteger(int resId)
    {
        return AndroidApplication.getInstance().getResources().getInteger(resId);
    }

    public static List<String> getStringList(int resId)
    {
        return Arrays.asList(AndroidApplication.getInstance().getResources().getStringArray(resId));
    }

    public static void openShareDialog(Context context, String dialogTitle, String shareContent)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        sendIntent.setType(DIALOG_SHARE_TYPE);
        context.startActivity(Intent.createChooser(sendIntent, dialogTitle));
    }

    public static void sendPlayMarketAppInfo(Context context)
    {
        AndroidUtils.openShareDialog(context,
                AndroidUtils.getString(R.string.tell_about_app_title),
                AndroidUtils.getString(R.string.play_market_app_http_link) + context.getPackageName());
    }

    public static void openAppByURI(Activity activity, String rawURI)
    {
        Uri uri = Uri.parse(rawURI);
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
        viewIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                | Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        startActivity(activity, viewIntent);
    }

    public static void openTelegramChannel(Activity activity, View view, String channelName)
    {
        try
        {
            openAppByURI(activity, getString(R.string.telegram_channel_uri) + channelName);
        }
        catch (ActivityNotFoundException e)
        {
            showSnackBarMessage(view, R.string.telegram_app_not_found);
        }
    }

    public static void openAppPage(Activity activity)
    {
        String packageName = activity.getPackageName();
        try
        {
            openAppByURI(activity, getString(R.string.play_market_app_link) + packageName);
        }
        catch (ActivityNotFoundException e)
        {
            startActivity(activity, new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.play_market_app_http_link) + packageName)));
        }
    }

    private static void startActivity(Activity activity, Intent intent)
    {
        activity.startActivity(intent);
    }

    public static void showSnackBarMessage(View whereShow, int textId)
    {
        showSnackBarMessage(whereShow, AndroidUtils.getString(textId));
    }

    public static void showSnackBarMessage(View whereShow, String message)
    {
        try
        {
            Snackbar
                .make(whereShow, message, Snackbar.LENGTH_SHORT)
                .show();
        }
        catch (Exception e)
        {
            LogUtils.error(LOG_TAG, "Ошибка при показе ShackBar сообщения!", e);
        }
    }

    public static boolean addWidgetFromAppSupported(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            AppWidgetManager appWidgetManager = context.getSystemService(AppWidgetManager.class);
            return appWidgetManager != null && appWidgetManager.isRequestPinAppWidgetSupported();
        }
        else
            return false;
    }

    public static void openAddWidgetDialog(Context context, Class clazz)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O
                && addWidgetFromAppSupported(context))
        {
            AppWidgetManager appWidgetManager = context.getSystemService(AppWidgetManager.class);
            ComponentName quoteWidgetProvider = new ComponentName(context, clazz);

            appWidgetManager.requestPinAppWidget(quoteWidgetProvider, null, null);
        }
    }
}
