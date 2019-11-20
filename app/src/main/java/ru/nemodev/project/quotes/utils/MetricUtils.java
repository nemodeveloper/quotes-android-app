package ru.nemodev.project.quotes.utils;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.commons.lang3.StringUtils;

import ru.nemodev.project.quotes.app.AndroidApplication;
import ru.nemodev.project.quotes.entity.purchase.Purchase;

public final class MetricUtils {

    private MetricUtils() { }

    public static void searchEvent(SearchType searchType, String whatSearch) {
        if (StringUtils.isEmpty(whatSearch))
            return;

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, searchType.name());
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, whatSearch);
        AndroidApplication.getAnalytics().logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
    }

    public static void shareEvent(ShareType shareType) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, shareType.name());
        bundle.putString(FirebaseAnalytics.Param.CONTENT, shareType.getTypeName());

        AndroidApplication.getAnalytics().logEvent(FirebaseAnalytics.Event.SHARE, bundle);
    }

    public static void inviteEvent(InviteType inviteType) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, inviteType.name());

        AndroidApplication.getAnalytics().logEvent(InviteType.EVENT_NAME, bundle);
    }

    public static void rateEvent(RateType rateType) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, rateType.name());
        bundle.putString(FirebaseAnalytics.Param.CONTENT, rateType.getRateName());

        AndroidApplication.getAnalytics().logEvent(RateType.EVENT_NAME, bundle);
    }

    public static void viewEvent(ViewType viewType) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, viewType.name());
        bundle.putString(FirebaseAnalytics.Param.CONTENT, viewType.getViewName());

        AndroidApplication.getAnalytics().logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    public static void purchaseEvent(Purchase purchase) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, purchase.getPurchaseType().getProductId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, purchase.getTitle());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, purchase.getPurchaseType().getItemType());
        bundle.putString(FirebaseAnalytics.Param.PRICE, purchase.getPrice().toString());
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, purchase.getCurrency().toString());
        bundle.putBoolean(FirebaseAnalytics.Param.SUCCESS, purchase.isPurchase());

        AndroidApplication.getAnalytics().logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);
    }

    public enum SearchType {
        AUTHOR,
        CATEGORY
    }

    public enum ShareType {
        QUOTE("Поделиться цитатой");

        private final String typeName;

        ShareType(String typeName)
        {
            this.typeName = typeName;
        }

        public String getTypeName()
        {
            return typeName;
        }
    }

    public enum InviteType {
        APP_LINK("Ссылка GooglePlay");

        private final String inviteName;

        InviteType(String inviteName)
        {
            this.inviteName = inviteName;
        }

        public String getInviteName()
        {
            return inviteName;
        }

        public static final String EVENT_NAME = "invite";
    }

    public enum RateType {
        APP("Оценка приложения"),
        QUOTE_LIKE("Лайк цитаты"),
        QUOTE_UNLIKE("Дизлайк цитаты");

        private final String rateName;

        RateType(String rateName)
        {
            this.rateName = rateName;
        }

        public String getRateName()
        {
            return rateName;
        }

        public static final String EVENT_NAME = "rate";
    }

    public enum ViewType {
        RANDOM_QUOTES("Случайные цитаты"),

        AUTHOR_LIST("Список авторов"),
        AUTHOR_QUOTES_FROM_MENU("Цитаты по автору"),
        AUTHOR_QUOTES_FROM_QUOTE_CARD("Цитаты по автору из карточки"),

        CATEGORY_LIST("Список категорий"),
        CATEGORY_QUOTES_FROM_MENU("Цитаты по категории"),
        CATEGORY_QUOTES_FROM_QUOTE_CARD("Цитаты по категории из карточки"),

        LIKED_QUOTES("Избранные цитаты"),

        PURCHASE_LIST("Просмотр списка покупок"),

        TELEGRAM_CHANNEL("Телеграм канал"),

        QUOTE_TO_WIDGET("Создание виджета из цитаты");

        private final String viewName;

        ViewType(String viewName)
        {
            this.viewName = viewName;
        }

        public String getViewName()
        {
            return viewName;
        }
    }
}
