package ru.nemodev.project.quotes.view;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.external.Author;
import ru.nemodev.project.quotes.entity.external.Category;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.view.author.search.AuthorFragment;
import ru.nemodev.project.quotes.view.category.search.CategoryFragment;
import ru.nemodev.project.quotes.view.quote.author.QuoteByAuthorFragment;
import ru.nemodev.project.quotes.view.quote.category.QuoteByCategoryFragment;
import ru.nemodev.project.quotes.view.quote.like.QuoteByLikeFragment;
import ru.nemodev.project.quotes.view.quote.random.QuoteByRandomFragment;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);
        // Обработка пунктов меню
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Показываем первоначальный контент
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        // Показываем рекламу
//        initAdb();
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
                openFragment(new QuoteByRandomFragment());
                break;
            }
            case R.id.nav_item_category:
            {
                openFragment(new CategoryFragment());
                break;
            }
            case R.id.nav_item_author:
            {
                openFragment(new AuthorFragment());
                break;
            }
            case R.id.nav_item_like:
            {
                openFragment(new QuoteByLikeFragment());
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
        QuoteByCategoryFragment quoteByCategoryFragment = new QuoteByCategoryFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(QuoteByCategoryFragment.CATEGORY_ID_KEY, category.getId());
        bundle.putString(QuoteByCategoryFragment.CATEGORY_NAME_KEY, category.getName());

        quoteByCategoryFragment.setArguments(bundle);

        openFragment(quoteByCategoryFragment);
    }

    public void openQuoteFragment(Author author)
    {
        QuoteByAuthorFragment quoteByAuthorFragment = new QuoteByAuthorFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(QuoteByAuthorFragment.AUTHOR_ID_KEY, author.getId());
        bundle.putString(QuoteByAuthorFragment.AUTHOR_NAME_KEY, author.getFullName());

        quoteByAuthorFragment.setArguments(bundle);

        openFragment(quoteByAuthorFragment);
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

//    private void initAdb()
//    {
//        MobileAds.initialize(this, getResources().getString(R.string.ads_app_id));
//        AdView adView = findViewById(R.id.adView);
//        adView.loadAd(new AdRequest.Builder().build());
//    }
}
