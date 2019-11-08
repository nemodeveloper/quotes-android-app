package ru.nemodev.project.quotes.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.entity.category.Category;
import ru.nemodev.project.quotes.ui.author.list.AuthorListFragmentDirections;
import ru.nemodev.project.quotes.ui.base.OnQuoteCardClickListener;
import ru.nemodev.project.quotes.ui.category.list.CategoryListFragmentDirections;
import ru.nemodev.project.quotes.ui.purchase.PurchaseListFragment;
import ru.nemodev.project.quotes.ui.purchase.PurchaseType;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.LogUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnQuoteCardClickListener, MainContract.MainView {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    NavController navController;
    AppBarConfiguration appBarConfiguration;

    private MainContract.MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        mainPresenter = new MainPresenterImpl(this, this);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navigationView.setNavigationItemSelectedListener(this);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_random_quote, R.id.navigation_author_list)
                        .setDrawerLayout(drawer)
                        .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mainPresenter.checkAppUpdate();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mainPresenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        mainPresenter.onDestroy();
        super.onDestroy();
    }

    // TODO обработка нажатия простых элементов не работает
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.navigation_purchase_list: {
                showPurchaseListView();
                return true;
            }
            case R.id.nav_item_telegram_channel: {
                MetricUtils.viewEvent(MetricUtils.ViewType.TELEGRAM_CHANNEL);
                AndroidUtils.openTelegramChannel(this, drawer, AndroidUtils.getString(R.string.telegram_channel_name));
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

        return false;//NavigationUI.onNavDestinationSelected(item, navController);
    }

    public void openQuoteFragment(Category category) {
        navController.navigate(CategoryListFragmentDirections.actionOpenCategoryDetail(category.getId(), category.getName()));
    }

    public void openQuoteFragment(Author author) {
        navController.navigate(AuthorListFragmentDirections.actionOpenAuthorDetail(author.getId(), author.getFullName()));
    }

    @Override
    public void onAuthorClick(Author author) {
        openQuoteFragment(author);
    }

    @Override
    public void onCategoryClick(Category category) {
        openQuoteFragment(category);
    }

    @Override
    public void showUpdateDialog() {
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

    // TODO перенести покупки и главный Activity на MVVM и навигацию
    private void showPurchaseListView() {
        showPurchaseListView(null);
    }

    private void showPurchaseListView(PurchaseType purchaseType) {
        PurchaseListFragment fragment = new PurchaseListFragment();
        if (purchaseType != null) {
            Bundle bundle = new Bundle();
            bundle.putString(PurchaseListFragment.PURCHASE_ID_ACTION, purchaseType.getProductId());
            fragment.setArguments(bundle);
        }
        fragment.setMainPresenter(mainPresenter);

    }

    @Override
    public void showDisableAdsDialog() {
        if (isFinishing())
            return;

        try {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.adb_disable_dialog_title)
                    .setPositiveButton(R.string.adb_disable_dialog_positive, (dialog, which) -> showPurchaseListView(PurchaseType.QUOTE_ADB))
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
