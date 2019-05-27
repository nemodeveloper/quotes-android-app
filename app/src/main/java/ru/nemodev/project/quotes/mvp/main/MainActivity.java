package ru.nemodev.project.quotes.mvp.main;

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

import com.crashlytics.android.Crashlytics;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.Author;
import ru.nemodev.project.quotes.entity.Category;
import ru.nemodev.project.quotes.mvp.author.detail.AuthorDetailFragment;
import ru.nemodev.project.quotes.mvp.author.list.AuthorListFragment;
import ru.nemodev.project.quotes.mvp.base.OnQuoteCardClickListener;
import ru.nemodev.project.quotes.mvp.category.detail.CategoryDetailFragment;
import ru.nemodev.project.quotes.mvp.category.list.CategoryListFragment;
import ru.nemodev.project.quotes.mvp.purchase.PurchaseListFragment;
import ru.nemodev.project.quotes.mvp.quote.liked.LikedQuoteListFragment;
import ru.nemodev.project.quotes.mvp.quote.random.RandomQuoteListFragment;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.LogUtils;

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
        initFabricIO();

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
                    AndroidUtils.openTelegramChannel(this);
                    break;
                }
                case R.id.nav_item_share: {
                    AndroidUtils.sendPlayMarketAppInfo(this);
                    break;
                }
                case R.id.nav_item_rate_app: {
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

    private void initFabricIO()
    {
        Fabric.with(this, new Crashlytics());
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
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_update_available_title)
                .setMessage(R.string.app_update_available_desc)
                .setPositiveButton(R.string.app_update_yes_text, (dialog, which) -> AndroidUtils.openAppPage(this))
                .setNeutralButton(R.string.app_update_pass_text, (dialog, which) -> {})
                .setCancelable(false)
                .create()
                .show();
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
        PurchaseListFragment fragment = new PurchaseListFragment();
        fragment.setMainPresenter(mainPresenter);

        openFragment(fragment);
    }

    @Override
    public void showDisableAdbDialog()
    {
        if (isFinishing())
            return;

        try
        {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.adb_disable_dialog_title)
                    .setPositiveButton(R.string.adb_disable_dialog_positive, (dialog, which) -> showPurchaseListView())
                    .setNeutralButton(R.string.adb_disable_dialog_neutral, (dialog, which) -> {})
                    .setCancelable(true)
                    .create()
                    .show();
        }
        catch (Exception e)
        {
            LogUtils.logWithReport(LOG_TAG, "Ошибка показа диалога отключения рекламы!", e);
        }
    }
}
