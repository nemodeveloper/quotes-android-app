package ru.nemodev.project.quotes.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

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
import ru.nemodev.project.quotes.ui.purchase.viewmodel.PurchaseViewModel;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.LogUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;
import ru.nemodev.project.quotes.widget.QuoteWidgetProvider;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, OnQuoteCardClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    NavController navController;
    AppBarConfiguration appBarConfiguration;

    private MainViewModel mainViewModel;
    private PurchaseViewModel purchaseViewModel;
    private MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.setActivity(this);

        purchaseViewModel = ViewModelProviders.of(this).get(PurchaseViewModel.class);
        purchaseViewModel.setActivity(this);

        setSupportActionBar(binding.toolbar);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setDrawerLayout(binding.drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.navView.setNavigationItemSelectedListener(this);

        mainViewModel.updateAppEvent.observe(this, aBoolean -> showUpdateDialog());
        mainViewModel.buyAdsEvent.observe(this, aBoolean -> showDisableAdsDialog());

        purchaseViewModel.onPurchaseEvent.observe(this, purchase -> mainViewModel.onPurchase(purchase));
        purchaseViewModel.onAdsByEvent.observe(this, mainViewModel::onAdsBuy);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        purchaseViewModel.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.nav_item_telegram_channel: {
                MetricUtils.viewEvent(MetricUtils.ViewType.TELEGRAM_CHANNEL);
                AndroidUtils.openTelegramChannel(this, binding.drawerLayout, AndroidUtils.getString(R.string.telegram_channel_name));
                return true;
            }
            case R.id.nav_item_share: {
                MetricUtils.inviteEvent(MetricUtils.InviteType.APP_LINK);
                AndroidUtils.sendPlayMarketAppInfo(this);
                return true;
            }
            case R.id.nav_item_rate_app: {
                MetricUtils.rateEvent(MetricUtils.RateType.APP);
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
        navController.navigate(CategoryListFragmentDirections.actionOpenCategoryDetail(category.getId(), category.getName()));
    }

    public void openQuoteFragment(Author author) {
        navController.navigate(AuthorListFragmentDirections.actionOpenAuthorDetail(author.getId(), author.getFullName()));
    }

    @Override
    public void onAuthorClick(Author author) {
        MetricUtils.viewEvent(MetricUtils.ViewType.AUTHOR_QUOTES_FROM_QUOTE_CARD);
        openQuoteFragment(author);
    }

    @Override
    public void onCategoryClick(Category category) {
        MetricUtils.viewEvent(MetricUtils.ViewType.CATEGORY_QUOTES_FROM_QUOTE_CARD);
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
                            MetricUtils.rateEvent(MetricUtils.RateType.QUOTE_LIKE);
                        }
                        else {
                            MetricUtils.rateEvent(MetricUtils.RateType.QUOTE_UNLIKE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.error(LOG_TAG, "Ошибка лайка цитаты!", e);
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void onShareClick(QuoteInfo quoteInfo) {
        MetricUtils.shareEvent(MetricUtils.ShareType.QUOTE);
        AndroidUtils.openShareDialog(this,
                AndroidUtils.getString(R.string.share_quote_dialog_title),
                QuoteUtils.getQuoteTextForShare(quoteInfo));
    }

    @Override
    public void onWidgetClick(QuoteInfo quoteInfo) {
        MetricUtils.viewEvent(MetricUtils.ViewType.QUOTE_TO_WIDGET);
        AndroidApplication.getInstance().getAppSetting().setLong(QuoteWidgetProvider.QUOTE_ID_BUNDLE_KEY, quoteInfo.getQuote().getId());
        AndroidUtils.openAddWidgetDialog(this, QuoteWidgetProvider.class);
    }

    private void showUpdateDialog() {
        if (!isFinishing()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_update_available_title)
                    .setMessage(R.string.app_update_available_desc)
                    .setPositiveButton(R.string.app_update_yes_text, (dialog, which) -> AndroidUtils.openAppPage(this))
                    .setNeutralButton(R.string.app_update_pass_text, (dialog, which) -> {})
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    private void showDisableAdsDialog() {
        if (isFinishing())
            return;

        try {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.adb_disable_dialog_title)
                    .setPositiveButton(R.string.adb_disable_dialog_positive, (dialog, which) -> purchaseViewModel.purchase(PurchaseType.QUOTE_ADB))
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
