package ru.nemodev.project.quotes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import ru.nemodev.core.app.AndroidApplication;
import ru.nemodev.core.utils.AndroidUtils;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.entity.quote.QuoteUtils;
import ru.nemodev.project.quotes.mvp.quote.QuoteInteractor;
import ru.nemodev.project.quotes.mvp.quote.QuoteInteractorImpl;


public class QuoteWidgetProvider extends AppWidgetProvider
{
    private static final String UPDATE_WIDGET_BUTTON_ACTION = "UPDATE_WIDGET_BUTTON_ACTION";
    private static final QuoteInteractor quoteLoader = new QuoteInteractorImpl();

    public static final String IS_PURCHASE_QUOTE_WIDGET_KEY = "IS_PURCHASE_QUOTE_WIDGET";

    public static final String QUOTE_ID_BUNDLE_KEY = "quote_id";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        if (appWidgetIds.length > 0)
        {
            final String packageName = context.getPackageName();

            for (int appWidgetId : appWidgetIds)
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
        if (AndroidApplication.getInstance().getAppSetting().getBoolean(IS_PURCHASE_QUOTE_WIDGET_KEY))
        {
            Long selectQuoteId = AndroidApplication.getInstance().getAppSetting().getLong(QUOTE_ID_BUNDLE_KEY);
            if (selectQuoteId != null && selectQuoteId != 0L)
            {
                quoteLoader.getById(selectQuoteId)
                        .subscribe(quoteInfo -> updateQuoteInfo(appWidgetManager, appWidgetId, remoteViews,
                                quoteInfo.getQuote().getText(), quoteInfo.getAuthor().getFullName()),
                                exception -> showErrorLoad(appWidgetManager, appWidgetId, remoteViews));
            }
            else
            {
                quoteLoader.loadRandom(new QuoteInteractor.OnFinishLoadListener()
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
                                updateQuoteInfo(appWidgetManager, appWidgetId, remoteViews,
                                        quoteInfo.getQuote().getText(), quoteInfo.getAuthor().getFullName());
                            }
                        }
                        else
                        {
                            showErrorLoad(appWidgetManager, appWidgetId, remoteViews);
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
                            showErrorLoad(appWidgetManager, appWidgetId, remoteViews);
                        }
                    }
                }, 50);
            }
        }
        else
        {
            showNotPurchaseInfo(appWidgetManager, appWidgetId, remoteViews);
        }

        AndroidApplication.getInstance().getAppSetting().removeValue(QuoteWidgetProvider.QUOTE_ID_BUNDLE_KEY);
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

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);

        AppWidgetHost appWidgetHost = new AppWidgetHost(context, 1);
        for (int widgetId : appWidgetIds)
        {
            appWidgetHost.deleteAppWidgetId(widgetId);
        }
    }

    private PendingIntent getPendingSelfIntent(Context context, String action, int appWidgetId)
    {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void updateQuoteInfo(AppWidgetManager appWidgetManager, final int appWidgetId, RemoteViews remoteViews,
                                 String quoteText, String authorText)
    {
        remoteViews.setTextViewText(R.id.quoteText, quoteText);
        remoteViews.setTextViewText(R.id.quoteAuthorName, QuoteUtils.QUOTE_AUTHOR_SYMBOL + authorText);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private void showErrorLoad(final AppWidgetManager appWidgetManager, final int appWidgetId, RemoteViews remoteViews)
    {
        updateQuoteInfo(appWidgetManager, appWidgetId, remoteViews,
                AndroidUtils.getString(R.string.widget_update_error), AndroidUtils.getString(R.string.dev_author_name));
    }

    private void showNotPurchaseInfo(final AppWidgetManager appWidgetManager, final int appWidgetId, RemoteViews remoteViews)
    {
        updateQuoteInfo(appWidgetManager, appWidgetId, remoteViews,
                AndroidUtils.getString(R.string.widget_not_purchase), AndroidUtils.getString(R.string.dev_author_name));
    }
}
