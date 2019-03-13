package ru.nemodev.project.quotes.mvp.category.list;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.Category;
import ru.nemodev.project.quotes.mvp.MainActivity;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;

public class CategoryListFragment extends BaseToolbarFragment implements CategoryListContract.CategoryListView
{
    private View root;
    private IndexFastScrollRecyclerView categoryLoadRV;
    private ProgressBar progressBar;
    private TextView notFullContentMessage;
    private SearchView searchView;

    private CategoryListContract.CategoryListPresenter presenter;

    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.category_fragmet, container, false);

        initToolbar(root);
        initRV(root);
        initProgressBar();
        initNotFullContentMessageBlock();

        presenter = new CategoryListPresenterImpl(this);
        presenter.loadCategory();

        connectToNetworkEvents();

        return root;
    }

    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
        toolbar.setTitle((AndroidUtils.getTextById(R.string.category_title)));

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        if (!isCanSearch())
        {
            super.onCreateOptionsMenu(menu, inflater);
        }
        else
        {
            inflater.inflate(R.menu.menu_category_bar, menu);

            searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setQueryHint(AndroidUtils.getTextById(R.string.category_search_hint));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
            {
                @Override
                public boolean onQueryTextSubmit(String query)
                {
                    searchCategory(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query)
                {
                    searchCategory(query);
                    return false;
                }
            });
            searchView.setOnSearchClickListener(v ->
            {
                categoryLoadRV.setIndexBarVisibility(false);

                CategoryListAdapter adapter = (CategoryListAdapter) categoryLoadRV.getAdapter();
                adapter.setAnimationEnable(false);
            });
            searchView.setOnCloseListener(() ->
            {
                categoryLoadRV.setIndexBarVisibility(true);

                CategoryListAdapter adapter = (CategoryListAdapter) categoryLoadRV.getAdapter();
                adapter.setAnimationEnable(true);

                return false;
            });
        }
    }

    private boolean isCanSearch()
    {
        CategoryListAdapter adapter = (CategoryListAdapter) categoryLoadRV.getAdapter();
        return adapter != null;
    }

    private void searchCategory(String search)
    {
        if (!isCanSearch())
            return;

        CategoryListAdapter adapter = (CategoryListAdapter) categoryLoadRV.getAdapter();
        adapter.getFilter().filter(search);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_search)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRV(View root)
    {
        categoryLoadRV = root.findViewById(R.id.categoryList);
        categoryLoadRV.setHasFixedSize(true);
        categoryLoadRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        categoryLoadRV.setIndexBarVisibility(false);
        categoryLoadRV.setIndexBarCornerRadius(0);
        categoryLoadRV.setIndexBarTransparentValue(0.5f);
        categoryLoadRV.setPreviewPadding(0);
        categoryLoadRV.setIndexbarMargin(0);
        categoryLoadRV.setIndexBarColor(R.color.search_bar_background);
    }

    private void initProgressBar()
    {
        progressBar = root.findViewById(R.id.contentLoadingProgressBar);
    }

    private void initNotFullContentMessageBlock()
    {
        notFullContentMessage = root.findViewById(R.id.not_full_content_message);
        notFullContentMessage.setOnClickListener(view -> setVisibleNotFullContentMessage(false));
    }

    private void connectToNetworkEvents()
    {
        disconnectFromNetworkEvents();
        internetEventsDisposable = NetworkUtils.getNetworkObservable()
                .subscribe(connectivity ->
                {
                    if (connectivity.state() == NetworkInfo.State.CONNECTED)
                    {
                        presenter.loadCategory();
                        setVisibleNotFullContentMessage(false);
                    }
                    else
                    {
                        setVisibleNotFullContentMessage(true);
                    }
                });
    }

    private void disconnectFromNetworkEvents()
    {
        if (internetEventsDisposable != null && !internetEventsDisposable.isDisposed())
            internetEventsDisposable.dispose();
    }

    @Override
    public void showLoader()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader()
    {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showCategories(List<Category> categories)
    {
        if (CollectionUtils.isNotEmpty(categories))
        {
            categoryLoadRV.setAdapter(new CategoryListAdapter(getActivity(), categories, item ->
            {
                searchView.clearFocus();
                MainActivity mainActivity1 = (MainActivity) getActivity();
                mainActivity1.openQuoteFragment(item);
            }));
            categoryLoadRV.setIndexBarVisibility(true);
            getActivity().invalidateOptionsMenu();
        }
    }

    private void setVisibleNotFullContentMessage(boolean isVisible)
    {
        if (isVisible)
            notFullContentMessage.setVisibility(View.VISIBLE);
        else
            notFullContentMessage.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyOptionsMenu()
    {
        if (searchView != null)
        {
            searchView.clearFocus();
        }
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onDestroy()
    {
        disconnectFromNetworkEvents();
        super.onDestroy();
    }

    @Override
    public void onPause()
    {
        disconnectFromNetworkEvents();
        super.onPause();
    }

    @Override
    public void onResume()
    {
        connectToNetworkEvents();
        super.onResume();
    }
}
