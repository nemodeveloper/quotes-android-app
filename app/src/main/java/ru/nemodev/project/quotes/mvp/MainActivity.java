package ru.nemodev.project.quotes.mvp;

import android.os.Bundle;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import io.fabric.sdk.android.Fabric;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.adb.BannerManager;
import ru.nemodev.project.quotes.entity.Author;
import ru.nemodev.project.quotes.entity.Category;
import ru.nemodev.project.quotes.mvp.author.detail.AuthorDetailFragment;
import ru.nemodev.project.quotes.mvp.author.list.AuthorListFragment;
import ru.nemodev.project.quotes.mvp.base.OnQuoteCardClickListener;
import ru.nemodev.project.quotes.mvp.category.detail.CategoryDetailFragment;
import ru.nemodev.project.quotes.mvp.category.list.CategoryListFragment;
import ru.nemodev.project.quotes.mvp.quote.liked.LikedQuoteListFragment;
import ru.nemodev.project.quotes.mvp.quote.random.RandomQuoteListFragment;
import ru.nemodev.project.quotes.utils.AndroidUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnQuoteCardClickListener
{
    private DrawerLayout drawer;
    protected NavigationView navigationView;
    private BannerManager bannerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initFabricIO();

        setContentView(R.layout.activity_main);
        initAdb();
        initNavigationMenu();
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
                    getSupportFragmentManager().popBackStack();
                    openFragment(new RandomQuoteListFragment());
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
                case R.id.nav_item_telegram_channel: {
                    AndroidUtils.openTelegramChannel(this);
                    break;
                }
                case R.id.nav_item_share: {
                    AndroidUtils.sendPlayMarketAppInfo(this);
                    break;
                }
                case R.id.nav_item_rate_app: {
                    AndroidUtils.openAppRatePage(this);
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
        drawer = findViewById(R.id.drawer_layout);

        // Обработка пунктов меню
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        navigationView.getMenu().getItem(0).setChecked(true);

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

    private void initAdb()
    {
        bannerManager = new BannerManager(this, findViewById(R.id.adView));
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
}
