package ru.nemodev.project.quotes.ui.category.list;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.ui.category.list.viewmodel.CategoryListViewModel;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class CategoryListFragment extends BaseToolbarFragment {
    private View root;

    @BindView(R.id.categoryList) RecyclerView categoryLoadRV;

    private SearchView searchView;
    private String whatSearch;

    private CategoryListViewModel viewModel;
    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.category_fragmet, container, false);
        ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(getActivity()).get(CategoryListViewModel.class);

        initToolbar();
        initRV();

        connectToNetworkEvents();
        MetricUtils.viewEvent(MetricUtils.ViewType.CATEGORY_LIST);

        return root;
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar();
        toolbar.setTitle((AndroidUtils.getString(R.string.category_title)));

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_category_bar, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(AndroidUtils.getString(R.string.category_search_hint));
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
    }

    private void searchCategory(String search) {
        if (StringUtils.isNotEmpty(search))
        {
            whatSearch = search;
            MetricUtils.searchEvent(MetricUtils.SearchType.AUTHOR, CategoryListFragment.this.whatSearch);
        }

        CategoryListAdapter adapter = (CategoryListAdapter) categoryLoadRV.getAdapter();
        viewModel.getCategoryList(this, whatSearch).observe(this, categories-> {
            adapter.submitList(categories, this::hideLoader);
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRV() {
        categoryLoadRV.setHasFixedSize(true);
        categoryLoadRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        CategoryListAdapter adapter = new CategoryListAdapter(getContext(), item -> {
            clearSearchFocus();
            MetricUtils.viewEvent(MetricUtils.ViewType.CATEGORY_QUOTES_FROM_MENU);
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.openQuoteFragment(item);
        });

        categoryLoadRV.setAdapter(adapter);
        searchCategory(whatSearch);

        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    private void connectToNetworkEvents() {
        disconnectFromNetworkEvents();
        internetEventsDisposable = NetworkUtils.getNetworkObservable()
                .subscribe(connectivity -> {
                    if (connectivity.state() == NetworkInfo.State.CONNECTED) {

                    }
                    else {
                        AndroidUtils.showSnackBarMessage(root, R.string.not_full_categories_message);
                    }
                });
    }

    private void disconnectFromNetworkEvents() {
        if (internetEventsDisposable != null && !internetEventsDisposable.isDisposed())
            internetEventsDisposable.dispose();
    }

    private void clearSearchFocus() {
        if (searchView != null) {
            searchView.clearFocus();
        }
    }

    @Override
    public void onDestroyOptionsMenu() {
        clearSearchFocus();
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onDestroy() {
        disconnectFromNetworkEvents();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        disconnectFromNetworkEvents();
        super.onPause();
    }

    @Override
    public void onResume() {
        connectToNetworkEvents();
        super.onResume();
    }
}
