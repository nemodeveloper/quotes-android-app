package ru.nemodev.project.quotes.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.widget.Toast;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.QuoteApp;

public final class AndroidUtils
{
    private static final String DIALOG_SHARE_TYPE = "text/plain";

    private AndroidUtils() { }

    public static String getTextById(int id)
    {
        return QuoteApp.getInstance().getResources().getString(id);
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
                AndroidUtils.getTextById(R.string.tell_about_app_title),
                AndroidUtils.getTextById(R.string.play_market_app_http_link) + context.getPackageName());
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

    public static void openTelegramChannel(Activity activity)
    {
        try
        {
            openAppByURI(activity, getTextById(R.string.telegram_channel_uri));
        }
        catch (ActivityNotFoundException e)
        {
            showToastMessage(R.string.telegram_app_not_found);
        }
    }

    public static void openAppRatePage(Activity activity)
    {
        String packageName = activity.getPackageName();

        try
        {
            openAppByURI(activity, getTextById(R.string.play_market_app_link) + packageName);
        }
        catch (ActivityNotFoundException e)
        {
            startActivity(activity, new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getTextById(R.string.play_market_app_http_link) + packageName)));
        }
    }

    private static void startActivity(Activity activity, Intent intent)
    {
        activity.startActivity(intent);
    }

    public static void showToastMessage(int messageResId)
    {
        Toast toast = Toast.makeText(QuoteApp.getInstance(), getTextById(messageResId), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0 ,0);
        toast.show();
    }
}
