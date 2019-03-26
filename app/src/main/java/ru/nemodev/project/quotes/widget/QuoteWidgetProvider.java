package ru.nemodev.project.quotes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.AppSetting;
import ru.nemodev.project.quotes.app.QuoteApp;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.entity.QuoteUtils;
import ru.nemodev.project.quotes.mvp.quote.QuoteIntractor;
import ru.nemodev.project.quotes.mvp.quote.QuoteIntractorImpl;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class QuoteWidgetProvider extends AppWidgetProvider
{
    private static final String UPDATE_WIDGET_BUTTON_ACTION = "UPDATE_WIDGET_BUTTON_ACTION";

    private static final QuoteIntractor quoteLoader = new QuoteIntractorImpl();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        if (appWidgetIds.length > 0)
        {
            final String packageName = context.getPackageName();

            for (int appWidgetId : appWidgetManager.getAppWidgetIds(new ComponentName(context, QuoteWidgetProvider.class)))
            {
                RemoteViews remoteViews = new RemoteViews(packageName, R.layout.quote_widget);
                remoteViews.setOnClickPendingIntent(R.id.updateWidgetButton,
                        getPendingSelfIntent(context, UPDATE_WIDGET_BUTTON_ACTION, appWidgetId));

                updateQuote(appWidgetManager, appWidgetId, remoteViews, false);
            }
        }
    }

    private void updateQuote(final AppWidgetManager appWidgetManager, final int appWidgetId, final RemoteViews remoteViews, final boolean fromCache)
    {
        if (QuoteApp.getInstance().getAppSetting().getBoolean(AppSetting.IS_PURCHASE_QUOTE_WIDGET_KEY))
        {
            quoteLoader.loadRandom(new QuoteIntractor.OnFinishLoadListener()
            {
                @Override
                public void onFinishLoad(List<QuoteInfo> quotes, boolean fromCache)
                {
                    if (CollectionUtils.isNotEmpty(quotes))
                    {
                        QuoteInfo quoteInfo = QuoteUtils.getQuoteForWidget(quotes);
                        if (quoteInfo == null)
                        {
                            updateQuote(appWidgetManager, appWidgetId, remoteViews, fromCache);
                        }
                        else
                        {
                            remoteViews.setTextViewText(R.id.quoteText, quoteInfo.getQuote().getText());
                            remoteViews.setTextViewText(R.id.quoteAuthorName, QuoteUtils.QUOTE_AUTHOR_SYMBOL + quoteInfo.getAuthor().getFullName());

                            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                        }
                    }
                    else
                    {
                        showErrorLoad(remoteViews);
                    }
                }

                @Override
                public void onLoadError(Throwable t, boolean fromCache)
                {
                    if (!fromCache)
                    {
                        updateQuote(appWidgetManager, appWidgetId, remoteViews, true);
                    }
                    else
                    {
                        showErrorLoad(remoteViews);
                    }
                }
            }, Collections.singletonMap("count", "50"), fromCache);
        }
        else
        {
            showNotPurchaseInfo(remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);

        if (UPDATE_WIDGET_BUTTON_ACTION.equals(intent.getAction()))
        {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.quote_widget);

            updateQuote(appWidgetManager,
                    intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0),
                    remoteViews, false);
        }
    }

    private PendingIntent getPendingSelfIntent(Context context, String action, int appWidgetId)
    {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void showErrorLoad(RemoteViews remoteViews)
    {
        remoteViews.setTextViewText(R.id.quoteText,
                AndroidUtils.getString(R.string.widget_update_error));
        remoteViews.setTextViewText(R.id.quoteAuthorName,
                QuoteUtils.QUOTE_AUTHOR_SYMBOL + AndroidUtils.getString(R.string.dev_author_name));
    }

    private void showNotPurchaseInfo(RemoteViews remoteViews)
    {
        remoteViews.setTextViewText(R.id.quoteText,
                AndroidUtils.getString(R.string.widget_not_purchase));
        remoteViews.setTextViewText(R.id.quoteAuthorName,
                QuoteUtils.QUOTE_AUTHOR_SYMBOL + AndroidUtils.getString(R.string.dev_author_name));
    }
}
