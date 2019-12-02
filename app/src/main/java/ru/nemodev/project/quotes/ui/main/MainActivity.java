package ru.nemodev.project.quotes.ui.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Calendar;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.AndroidApplication;
import ru.nemodev.project.quotes.databinding.MainActivityBinding;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.entity.category.Category;
import ru.nemodev.project.quotes.entity.purchase.PurchaseType;
import ru.nemodev.project.quotes.entity.quote.Quote;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.entity.quote.QuoteUtils;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;
import ru.nemodev.project.quotes.ui.author.list.AuthorListFragmentDirections;
import ru.nemodev.project.quotes.ui.base.OnQuoteCardClickListener;
import ru.nemodev.project.quotes.ui.category.list.CategoryListFragmentDirections;
import ru.nemodev.project.quotes.ui.main.viewmodel.MainViewModel;
import ru.nemodev.project.quotes.ui.main.viewmodel.MainViewModelFactory;
import ru.nemodev.project.quotes.ui.main.viewmodel.UpdateAppViewModel;
import ru.nemodev.project.quotes.ui.main.viewmodel.UpdateAppViewModelFactory;
import ru.nemodev.project.quotes.ui.purchase.viewmodel.PurchaseViewModel;
import ru.nemodev.project.quotes.ui.purchase.viewmodel.PurchaseViewModelFactory;
import ru.nemodev.project.quotes.utils.AnalyticUtils;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.LogUtils;
import ru.nemodev.project.quotes.widget.QuoteWidgetProvider;
import ru.nemodev.project.quotes.widget.WidgetUtils;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, OnQuoteCardClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    private MainViewModel mainViewModel;
    private PurchaseViewModel purchaseViewModel;
    private UpdateAppViewModel updateAppViewModel;
    private MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        mainViewModel = ViewModelProviders.of(this, new MainViewModelFactory(this)).get(MainViewModel.class);
        purchaseViewModel = ViewModelProviders.of(this, new PurchaseViewModelFactory(this)).get(PurchaseViewModel.class);
        updateAppViewModel = ViewModelProviders.of(this, new UpdateAppViewModelFactory(this)).get(UpdateAppViewModel.class);

        // navigation
        setSupportActionBar(binding.toolbar);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setDrawerLayout(binding.drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.navView.setNavigationItemSelectedListener(this);

        // main viewmodel
        mainViewModel.buyAdsRequest.observe(this, aBoolean -> showDisableAdsDialog());
        updateAppViewModel.updateAppEvent.observe(this, installState -> this.showUpdateDialog());

        // purchase viewmodel
        purchaseViewModel.onPurchaseEvent.observe(this, purchase -> mainViewModel.onPurchase(purchase));
        purchaseViewModel.onAdsByEvent.observe(this, mainViewModel::onAdsBuy);
        purchaseViewModel.onWidgetByEvent.observe(this, mainViewModel::onWidgetBuy);
        purchaseViewModel.purchaseList.observe(this, purchaseItems -> purchaseViewModel.checkPurchase());
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.nav_item_telegram_channel: {
                AnalyticUtils.viewEvent(AnalyticUtils.ViewType.TELEGRAM_CHANNEL);
                AndroidUtils.openTelegramChannel(this, binding.drawerLayout, AndroidUtils.getString(R.string.telegram_channel_name));
                return true;
            }
            case R.id.nav_item_share: {
                AnalyticUtils.inviteEvent(AnalyticUtils.InviteType.APP_LINK);
                AndroidUtils.sendPlayMarketAppInfo(this);
                return true;
            }
            case R.id.nav_item_rate_app: {
                AnalyticUtils.rateEvent(AnalyticUtils.RateType.APP);
                AndroidUtils.openAppPage(this);
                return true;
            }
        }

        boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
        if (handled) {
            binding.drawerLayout.closeDrawer(binding.navView);
        }

        return handled;
    }

    public void openQuoteFragment(Category category) {
        navigate(CategoryListFragmentDirections.actionOpenCategoryDetail(category.getId(), category.getName()));
    }

    public void openQuoteFragment(Author author) {
        navigate(AuthorListFragmentDirections.actionOpenAuthorDetail(author.getId(), author.getFullName()));
    }

    private void navigate(@NonNull NavDirections directions) {
        if (navController.getCurrentDestination() != null
                && navController.getCurrentDestination().getAction(directions.getActionId()) != null
                && navController.getCurrentDestination().getId() != directions.getActionId()) {
            navController.navigate(directions);
        }
    }

    @Override
    public void onAuthorClick(Author author) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, author.getFullName());

        AnalyticUtils.viewEvent(AnalyticUtils.ViewType.AUTHOR_QUOTES_FROM_QUOTE_CARD, bundle);
        openQuoteFragment(author);
    }

    @Override
    public void onCategoryClick(Category category) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, category.getName());

        AnalyticUtils.viewEvent(AnalyticUtils.ViewType.CATEGORY_QUOTES_FROM_QUOTE_CARD, bundle);
        openQuoteFragment(category);
    }

    @Override
    public void onLikeClick(QuoteInfo quoteInfo) {
        Quote likeQuote = quoteInfo.getQuote();
        likeQuote.setLiked(!likeQuote.getLiked());
        likeQuote.setLikeDate(likeQuote.getLiked() ? Calendar.getInstance() : null);

        AppDataBase.getInstance().getQuoteRepository().likeAsync(quoteInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QuoteInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(QuoteInfo quote) {
                        if (quoteInfo.getQuote().getLiked()) {
                            AnalyticUtils.rateEvent(AnalyticUtils.RateType.QUOTE_LIKE);
                        }
                        else {
                            AnalyticUtils.rateEvent(AnalyticUtils.RateType.QUOTE_UNLIKE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.error(LOG_TAG, "Ошибка лайка цитаты!", e);
                    }

                    @Override
                    public void onComplete() { }
                });

        AndroidUtils.showSnackBarMessage(binding.getRoot(),
                likeQuote.getLiked()
                        ? R.string.quote_action_like_notify
                        : R.string.quote_action_unlike_notify);
    }

    @Override
    public void onShareClick(QuoteInfo quoteInfo) {
        AnalyticUtils.shareEvent(AnalyticUtils.ShareType.QUOTE);
        AndroidUtils.openShareDialog(this,
                AndroidUtils.getString(R.string.share_quote_dialog_title),
                QuoteUtils.getQuoteTextForShare(quoteInfo));
    }

    @Override
    public void onWidgetClick(QuoteInfo quoteInfo) {
        AnalyticUtils.viewEvent(AnalyticUtils.ViewType.QUOTE_TO_WIDGET);
        AndroidApplication.getInstance().getAppSetting().setLong(WidgetUtils.WIDGET_QUOTE_ID_KEY, quoteInfo.getQuote().getId());
        AndroidUtils.openAddWidgetDialog(this, QuoteWidgetProvider.class);
    }

    @Override
    public void onCopyClick(QuoteInfo quoteInfo) {
        AnalyticUtils.shareEvent(AnalyticUtils.ShareType.QUOTE_COPY);
        AndroidUtils.copyTextToClipBoard(QuoteUtils.getQuoteTextForShare(quoteInfo));
        AndroidUtils.showSnackBarMessage(binding.getRoot(), R.string.quote_action_copy_notify);
    }

    private void showUpdateDialog() {
        if (!isFinishing()) {
            try {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.app_update_ready_title)
                        .setPositiveButton(R.string.app_update_yes, (dialog, which) -> updateAppViewModel.appUpdate())
                        .setNeutralButton(R.string.app_update_no, (dialog, which) -> {})
                        .setCancelable(true)
                        .create()
                        .show();
            }
            catch (Exception e) {
                LogUtils.error(LOG_TAG, "Ошибка показа диалога обновления приложения!", e);
            }
        }
    }

    private void showDisableAdsDialog() {
        if (isFinishing())
            return;

        try {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.adb_disable_dialog_title)
                    .setPositiveButton(R.string.adb_disable_dialog_positive, (dialog, which) -> purchaseViewModel.buy(PurchaseType.QUOTE_ADB))
                    .setNeutralButton(R.string.adb_disable_dialog_neutral, (dialog, which) -> {})
                    .setCancelable(true)
                    .create()
                    .show();
        }
        catch (Exception e) {
            LogUtils.error(LOG_TAG, "Ошибка показа диалога отключения рекламы!", e);
        }
    }
}
