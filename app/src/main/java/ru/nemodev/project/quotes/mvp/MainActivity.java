package ru.nemodev.project.quotes.mvp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import io.fabric.sdk.android.Fabric;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.external.Author;
import ru.nemodev.project.quotes.entity.external.Category;
import ru.nemodev.project.quotes.mvp.author.detail.AuthorDetailFragment;
import ru.nemodev.project.quotes.mvp.author.list.AuthorListFragment;
import ru.nemodev.project.quotes.mvp.category.detail.CategoryDetailFragment;
import ru.nemodev.project.quotes.mvp.category.list.CategoryListFragment;
import ru.nemodev.project.quotes.mvp.quote.random.RandomQuoteListFragment;
import ru.nemodev.project.quotes.utils.AndroidUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initFabricIO();
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);
        // Обработка пунктов меню
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Показываем первоначальный контент
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        //Показываем рекламу
        initAdb();
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

        switch (id)
        {
            case R.id.nav_item_random:
            {
                openFragment(new RandomQuoteListFragment());
                break;
            }
            case R.id.nav_item_category:
            {
                openFragment(new CategoryListFragment());
                break;
            }
            case R.id.nav_item_author:
            {
                openFragment(new AuthorListFragment());
                break;
            }
            case R.id.nav_item_share:
            {
                AndroidUtils.sendPlayMarketAppInfo(this);
                break;
            }
            case R.id.nav_item_telegram_channel:
            {
                AndroidUtils.openAppByURI(this, AndroidUtils.getTextById(R.string.telegram_channel_uri));
                break;
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

    private void initAdb()
    {
        MobileAds.initialize(this, getResources().getString(R.string.ads_app_id));
        AdView adView = findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    private void initFabricIO()
    {
        Fabric.with(this, new Crashlytics());
    }
}
