package ru.nemodev.project.quotes.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.entity.category.Category;
import ru.nemodev.project.quotes.ui.author.detail.AuthorDetailFragment;
import ru.nemodev.project.quotes.ui.author.list.AuthorListFragment;
import ru.nemodev.project.quotes.ui.base.OnQuoteCardClickListener;
import ru.nemodev.project.quotes.ui.category.detail.CategoryDetailFragment;
import ru.nemodev.project.quotes.ui.category.list.CategoryListFragment;
import ru.nemodev.project.quotes.ui.purchase.PurchaseListFragment;
import ru.nemodev.project.quotes.ui.purchase.PurchaseType;
import ru.nemodev.project.quotes.ui.quote.liked.LikedQuoteListFragment;
import ru.nemodev.project.quotes.ui.quote.random.RandomQuoteListFragment;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.LogUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, OnQuoteCardClickListener, MainContract.MainView
{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private MainContract.MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initNavigationMenu();

        mainPresenter = new MainPresenterImpl(this, this);
        showMainContent();

        mainPresenter.checkAppUpdate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        mainPresenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy()
    {
        mainPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() == 1)
        {
            finish();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (!item.isChecked())
        {
            switch (id) {
                case R.id.nav_item_random: {
                    showMainContent();
                    break;
                }
                case R.id.nav_item_category: {
                    getSupportFragmentManager().popBackStack();
                    openFragment(new CategoryListFragment());
                    break;
                }
                case R.id.nav_item_author: {
                    getSupportFragmentManager().popBackStack();
                    openFragment(new AuthorListFragment());
                    break;
                }
                case R.id.nav_item_like: {
                    getSupportFragmentManager().popBackStack();
                    openFragment(new LikedQuoteListFragment());
                    break;
                }
                case R.id.nav_item_purchase: {
                    showPurchaseListView();
                    break;
                }
                case R.id.nav_item_telegram_channel: {
                    MetricUtils.viewEvent(MetricUtils.ViewType.TELEGRAM_CHANNEL);
                    AndroidUtils.openTelegramChannel(this, drawer, AndroidUtils.getString(R.string.telegram_channel_name));
                    break;
                }
                case R.id.nav_item_share: {
                    MetricUtils.inviteEvent(MetricUtils.InviteType.APP_LINK);
                    AndroidUtils.sendPlayMarketAppInfo(this);
                    break;
                }
                case R.id.nav_item_rate_app: {
                    MetricUtils.rateEvent(MetricUtils.RateType.APP);
                    AndroidUtils.openAppPage(this);
                    break;
                }
            }
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void openQuoteFragment(Category category)
    {
        CategoryDetailFragment categoryDetailFragment = new CategoryDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(CategoryDetailFragment.CATEGORY_ID_KEY, category.getId());
        bundle.putString(CategoryDetailFragment.CATEGORY_NAME_KEY, category.getName());

        categoryDetailFragment.setArguments(bundle);
        openFragment(categoryDetailFragment);
    }

    public void openQuoteFragment(Author author)
    {
        AuthorDetailFragment authorDetailFragment = new AuthorDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(AuthorDetailFragment.AUTHOR_ID_KEY, author.getId());
        bundle.putString(AuthorDetailFragment.AUTHOR_NAME_KEY, author.getFullName());

        authorDetailFragment.setArguments(bundle);
        openFragment(authorDetailFragment);
    }

    private void openFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.viewContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void initNavigationMenu()
    {
        // Обработка пунктов меню
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(() -> {
            if (fragmentManager.getBackStackEntryCount() == 1)
            {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            else
            {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        });
    }

    @Override
    public void onAuthorClick(Author author)
    {
        openQuoteFragment(author);
    }

    @Override
    public void onCategoryClick(Category category)
    {
        openQuoteFragment(category);
    }

    @Override
    public void showUpdateDialog()
    {
        if (!this.isFinishing())
        {
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

    @Override
    public void showMainContent()
    {
        navigationView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().popBackStack();
        openFragment(new RandomQuoteListFragment());
    }

    private void showPurchaseListView()
    {
        showPurchaseListView(null);
    }

    private void showPurchaseListView(PurchaseType purchaseType)
    {
        PurchaseListFragment fragment = new PurchaseListFragment();
        if (purchaseType != null)
        {
            Bundle bundle = new Bundle();
            bundle.putString(PurchaseListFragment.PURCHASE_ID_ACTION, purchaseType.getProductId());
            fragment.setArguments(bundle);
        }
        fragment.setMainPresenter(mainPresenter);

        openFragment(fragment);
    }

    @Override
    public void showDisableAdsDialog()
    {
        if (isFinishing())
            return;

        try
        {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.adb_disable_dialog_title)
                    .setPositiveButton(R.string.adb_disable_dialog_positive, (dialog, which) -> showPurchaseListView(PurchaseType.QUOTE_ADB))
                    .setNeutralButton(R.string.adb_disable_dialog_neutral, (dialog, which) -> {})
                    .setCancelable(true)
                    .create()
                    .show();
        }
        catch (Exception e)
        {
            LogUtils.error(LOG_TAG, "Ошибка показа диалога отключения рекламы!", e);
        }
    }
}