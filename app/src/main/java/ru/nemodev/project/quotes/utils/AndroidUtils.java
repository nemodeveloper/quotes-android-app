package ru.nemodev.project.quotes.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.QuoteApp;

public final class AndroidUtils
{
    private static final String DIALOG_SHARE_TYPE = "text/plain";

    private AndroidUtils() { }

    public static String getString(int resId)
    {
        return QuoteApp.getInstance().getResources().getString(resId);
    }

    public static List<String> getStringList(int resId)
    {
        return Arrays.asList(QuoteApp.getInstance().getResources().getStringArray(resId));
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
        MetricUtils.inviteEvent(MetricUtils.InviteType.APP_LINK);
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

    public static void openTelegramChannel(Activity activity)
    {
        try
        {
            MetricUtils.viewEvent(MetricUtils.ViewType.TELEGRAM_CHANNEL);
            openAppByURI(activity, getString(R.string.telegram_channel_uri));
        }
        catch (ActivityNotFoundException e)
        {
            showToastMessage(R.string.telegram_app_not_found);
        }
    }

    public static void openAppRatePage(Activity activity)
    {
        MetricUtils.rateEvent(MetricUtils.RateType.APP);

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

    public static void showToastMessage(int messageResId)
    {
        Toast toast = Toast.makeText(QuoteApp.getInstance(), getString(messageResId), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0 ,0);
        toast.show();
    }

    public static void showSnackBarMessage(View whereShow, int textId)
    {
        Snackbar snackbar = Snackbar
                .make(whereShow, AndroidUtils.getString(textId), Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
