package ru.nemodev.project.quotes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import ru.nemodev.project.quotes.R;


public final class WidgetUtils {

    public static final String IS_PURCHASE_QUOTE_WIDGET_KEY = "IS_PURCHASE_QUOTE_WIDGET";

    public static final String UPDATE_WIDGET_BUTTON_ACTION = "UPDATE_WIDGET_BUTTON_ACTION";

    public static final String WIDGET_QUOTE_ID_BUNDLE_KEY = "widget_quote_id";
    public static final String QUOTE_ID_BUNDLE_KEY = "quote_id";


    public static RemoteViews getWidgetView(Context context, int widgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.quote_widget);
        remoteViews.setOnClickPendingIntent(R.id.updateWidgetButton,
                getPendingSelfIntent(context, UPDATE_WIDGET_BUTTON_ACTION, widgetId));

        return remoteViews;
    }

    public static PendingIntent getPendingSelfIntent(Context context, String action, int widgetId) {
        Intent intent = new Intent(context, QuoteWidgetProvider.class);
        intent.setAction(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        return PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
