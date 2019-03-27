package ru.nemodev.project.quotes.utils;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.InviteEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.crashlytics.android.answers.RatingEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.crashlytics.android.answers.ShareEvent;

import org.apache.commons.lang3.StringUtils;

import ru.nemodev.project.quotes.entity.Purchase;

public final class MetricUtils
{
    private static final String WHAT_SEARCH_KEY = "WHAT_SEARCH";

    private MetricUtils() { }

    public static void searchEvent(SearchType searchType, String whatSearch)
    {
        if (StringUtils.isEmpty(whatSearch))
            return;

        Answers.getInstance().logSearch(new SearchEvent()
                .putQuery(searchType.name())
                .putCustomAttribute(WHAT_SEARCH_KEY, whatSearch));
    }

    public static void shareEvent(ShareType shareType)
    {
        Answers.getInstance().logShare(new ShareEvent()
                .putContentType(shareType.name())
                .putContentName(shareType.getTypeName()));
    }

    public static void inviteEvent(InviteType inviteType)
    {
        Answers.getInstance().logInvite(new InviteEvent().putMethod(inviteType.name()));
    }

    public static void rateEvent(RateType rateType)
    {
        Answers.getInstance().logRating(new RatingEvent()
                .putContentType(rateType.name())
                .putContentName(rateType.getRateName()));
    }

    public static void viewEvent(ViewType viewType)
    {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentType(viewType.name())
                .putContentName(viewType.getViewName()));
    }

    public static void purchaseEvent(Purchase purchase)
    {
        Answers.getInstance().logPurchase(new PurchaseEvent()
                .putItemId(purchase.getPurchaseType().getProductId())
                .putItemName(purchase.getTitle())
                .putItemType(purchase.getPurchaseType().getItemType())
                .putItemPrice(purchase.getPrice())
                .putCurrency(purchase.getCurrency())
                .putSuccess(purchase.isPurchase())
        );
    }

    public enum SearchType
    {
        AUTHOR,
        CATEGORY
    }

    public enum ShareType
    {
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

    public enum InviteType
    {
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
    }

    public enum RateType
    {
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
    }

    public enum ViewType
    {
        RANDOM_QUOTES("Случайные цитаты"),

        AUTHOR_LIST("Список авторов"),
        AUTHOR_QUOTES_FROM_MENU("Цитаты по автору"),
        AUTHOR_QUOTES_FROM_QUOTE_CARD("Цитаты по автору из карточки"),

        CATEGORY_LIST("Список категорий"),
        CATEGORY_QUOTES_FROM_MENU("Цитаты по категории"),
        CATEGORY_QUOTES_FROM_QUOTE_CARD("Цитаты по категории из карточки"),

        LIKED_QUOTES("Избранные цитаты"),

        TELEGRAM_CHANNEL("Телеграм канал"),

        FULL_SCREEN_BANNER("Показ полноэкранного баннера");

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
