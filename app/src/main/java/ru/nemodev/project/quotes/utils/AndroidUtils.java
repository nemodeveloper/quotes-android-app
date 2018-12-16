package ru.nemodev.project.quotes.utils;

import android.app.Activity;
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

    public static void openAppByURI(Activity activity, String uri)
    {
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
    }

    public static void showToastMessage(int messageResId)
    {
        Toast toast = Toast.makeText(QuoteApp.getInstance(), getTextById(messageResId), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0 ,0);
        toast.show();
    }
}
