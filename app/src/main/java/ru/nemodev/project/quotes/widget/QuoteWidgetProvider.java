package ru.nemodev.project.quotes.widget;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.AndroidApplication;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.entity.quote.QuoteUtils;
import ru.nemodev.project.quotes.service.quote.QuoteService;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class QuoteWidgetProvider extends AppWidgetProvider {

    private static final QuoteService quoteService = QuoteService.getInstance();

    private Context context;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (appWidgetIds.length > 0) {
            updateWidgets(context, appWidgetIds);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (WidgetUtils.UPDATE_WIDGET_BUTTON_ACTION.equals(intent.getAction())) {
            int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            updateWidgets(context, widgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        AppWidgetHost appWidgetHost = new AppWidgetHost(context, 1);
        for (int widgetId : appWidgetIds) {
            appWidgetHost.deleteAppWidgetId(widgetId);
        }
    }

    private void updateWidgets(Context context, int... widgetIds) {
        this.context = context;
        try {
            for (int widgetId : widgetIds) {
                RemoteViews remoteViews = WidgetUtils.getWidgetView(context, widgetId);
                updateQuote(widgetId, remoteViews);
            }
        }
        finally {
            this.context = null;
        }
    }

    private void updateQuote(final int appWidgetId, final RemoteViews remoteViews) {
        if (AndroidApplication.getInstance().getAppSetting().getBoolean(WidgetUtils.IS_PURCHASE_QUOTE_WIDGET_KEY)) {
            Long selectQuoteId = AndroidApplication.getInstance().getAppSetting().getLong(WidgetUtils.WIDGET_QUOTE_ID_KEY);
            if (selectQuoteId != null && selectQuoteId != 0L) {
                AndroidApplication.getInstance().getAppSetting().removeValue(WidgetUtils.WIDGET_QUOTE_ID_KEY);
                try {
                    QuoteInfo quoteInfo = quoteService.getById(selectQuoteId).blockingFirst();
                    updateQuoteInfo(appWidgetId, remoteViews,
                            quoteInfo.getQuote().getText(),
                            quoteInfo.getAuthor().getFullName());
                }
                catch (Exception e) {
                    showErrorLoad(appWidgetId, remoteViews);
                }
            }
            else {
                try {
                    List<QuoteInfo> quoteInfos = quoteService.getRandomForWidget().blockingFirst();
                    if (CollectionUtils.isNotEmpty(quoteInfos)) {
                        QuoteInfo quoteInfo = quoteInfos.get(0);
                        updateQuoteInfo(appWidgetId, remoteViews,
                                quoteInfo.getQuote().getText(),
                                quoteInfo.getAuthor().getFullName());
                    }
                    else {
                        showErrorLoad(appWidgetId, remoteViews);
                    }
                }
                catch (Exception e) {
                    showErrorLoad(appWidgetId, remoteViews);
                }
            }
        }
        else {
            showNotPurchaseInfo(appWidgetId, remoteViews);
        }
    }

    private void updateQuoteInfo(final int appWidgetId, RemoteViews remoteViews,
                                 String quoteText, String authorText) {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        remoteViews.setTextViewText(R.id.quoteText, quoteText);
        remoteViews.setTextViewText(R.id.quoteAuthorName, QuoteUtils.QUOTE_AUTHOR_SYMBOL + authorText);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private void showErrorLoad(final int appWidgetId, RemoteViews remoteViews) {
        updateQuoteInfo(appWidgetId, remoteViews,
                AndroidUtils.getString(R.string.widget_update_error), AndroidUtils.getString(R.string.dev_author_name));
    }

    private void showNotPurchaseInfo(final int appWidgetId, RemoteViews remoteViews) {
        updateQuoteInfo(appWidgetId, remoteViews,
                AndroidUtils.getString(R.string.widget_not_purchase), AndroidUtils.getString(R.string.dev_author_name));
    }
}
