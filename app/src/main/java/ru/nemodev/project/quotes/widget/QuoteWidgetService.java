package ru.nemodev.project.quotes.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.AndroidApplication;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.entity.quote.QuoteUtils;
import ru.nemodev.project.quotes.service.quote.QuoteService;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class QuoteWidgetService extends JobIntentService {

    private static final QuoteService quoteService = QuoteService.getInstance();

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        int result = super.onStartCommand(intent, flags, startId);
        if (result == JobIntentService.START_NOT_STICKY) {
            onHandleWork(intent);
        }

        return result;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        int[] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : widgetIds) {
            RemoteViews remoteViews = WidgetUtils.getWidgetView(getApplicationContext(), widgetId);
            updateQuote(widgetId, remoteViews);
        }
    }

    private void updateQuote(final int appWidgetId, final RemoteViews remoteViews) {
        if (AndroidApplication.getInstance().getAppSetting().getBoolean(WidgetUtils.IS_PURCHASE_QUOTE_WIDGET_KEY)) {
            Long selectQuoteId = AndroidApplication.getInstance().getAppSetting().getLong(WidgetUtils.QUOTE_ID_BUNDLE_KEY);
            if (selectQuoteId != null && selectQuoteId != 0L) {
                AndroidApplication.getInstance().getAppSetting().removeValue(WidgetUtils.QUOTE_ID_BUNDLE_KEY);

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
                    List<QuoteInfo> quoteInfos = quoteService.getRandomForWidget(50).blockingFirst();
                    if (CollectionUtils.isNotEmpty(quoteInfos)) {
                        QuoteInfo quoteInfo = QuoteUtils.getQuoteForWidget(quoteInfos);
                        if (quoteInfo == null) {
                            updateQuote(appWidgetId, remoteViews);
                        }
                        else {
                            updateQuoteInfo(appWidgetId, remoteViews,
                                    quoteInfo.getQuote().getText(),
                                    quoteInfo.getAuthor().getFullName());
                        }
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
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
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
