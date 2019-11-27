package ru.nemodev.project.quotes.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.AndroidApplication;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.entity.quote.QuoteUtils;
import ru.nemodev.project.quotes.service.quote.QuoteService;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.LogUtils;


public class QuoteWidgetService extends Service {

    private static final String LOG_TAG = QuoteWidgetService.class.getSimpleName();

    private static final QuoteService quoteService = QuoteService.getInstance();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int[] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        quoteService.getRandom(100).subscribe(quoteInfos ->
                LogUtils.info(LOG_TAG, "Синхронизация цитат для виджета успешно завершена!"));

        for (int widgetId : widgetIds) {
            RemoteViews remoteViews = WidgetUtils.getWidgetView(getBaseContext(), widgetId);
            updateQuote(widgetId, remoteViews);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateQuote(final int appWidgetId, final RemoteViews remoteViews) {
        // TODO ID цитаты всегда 0 + корректно ли беретеся флаг покупки виджета
        if (AndroidApplication.getInstance().getAppSetting().getBoolean(WidgetUtils.IS_PURCHASE_QUOTE_WIDGET_KEY)) {
            Long selectQuoteId = AndroidApplication.getInstance().getAppSetting().getLong(WidgetUtils.WIDGET_QUOTE_ID_BUNDLE_KEY);
            if (selectQuoteId != null && selectQuoteId != 0L) {
                quoteService.getById(selectQuoteId)
                        .subscribe(quoteInfo -> updateQuoteInfo(appWidgetId, remoteViews,
                                quoteInfo.getQuote().getText(), quoteInfo.getAuthor().getFullName()),
                                exception -> showErrorLoad(appWidgetId, remoteViews));
            }
            else {
                quoteService.getRandomForWidget(50)
                        .subscribe(new Observer<List<QuoteInfo>>() {
                            @Override
                            public void onSubscribe(Disposable d) { }

                            @Override
                            public void onNext(List<QuoteInfo> quoteInfos) {
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

                            @Override
                            public void onError(Throwable e) {
                                showErrorLoad(appWidgetId, remoteViews);
                            }

                            @Override
                            public void onComplete() { }
                        });
            }
        }
        else {
            showNotPurchaseInfo(appWidgetId, remoteViews);
        }

        AndroidApplication.getInstance().getAppSetting().removeValue(WidgetUtils.QUOTE_ID_BUNDLE_KEY);
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
