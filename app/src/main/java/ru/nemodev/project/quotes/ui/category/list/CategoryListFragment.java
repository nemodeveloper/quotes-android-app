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
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.apache.commons.lang3.StringUtils;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.databinding.CategoryListFragmentBinding;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.category.list.viewmodel.CategoryListViewModel;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class CategoryListFragment extends BaseFragment {

    private SearchView searchView;
    private CategoryListFragmentBinding binding;
    private CategoryListViewModel viewModel;

    public CategoryListFragment() {
        super(R.layout.category_list_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(CategoryListViewModel.class);
        binding = DataBindingUtil.bind(root);

        setHasOptionsMenu(true);
        initialize();

        connectToNetworkEvents();
        MetricUtils.viewEvent(MetricUtils.ViewType.CATEGORY_LIST);

        return root;
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
        showLoader();

        if (StringUtils.isNotEmpty(search)) {
            MetricUtils.searchEvent(MetricUtils.SearchType.AUTHOR, search);
        }

        CategoryListAdapter adapter = (CategoryListAdapter) binding.categoryList.getAdapter();
        viewModel.getCategoryList(this, search).observe(this,
                categories-> adapter.submitList(categories, this::hideLoader));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        showLoader();

        viewModel.startWorkEvent.observe(this, aBoolean -> {
            if (aBoolean) {
                showLoader();
            }
            else {
                hideLoader();
            }
        });

        binding.categoryList.setHasFixedSize(true);
        binding.categoryList.setLayoutManager(new LinearLayoutManager(getActivity()));

        CategoryListAdapter adapter = new CategoryListAdapter(getContext(), item -> {
            clearSearchFocus();
            MetricUtils.viewEvent(MetricUtils.ViewType.CATEGORY_QUOTES_FROM_MENU);
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.openQuoteFragment(item);
        });

        binding.categoryList.setAdapter(adapter);
        searchCategory(null);

        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    private void connectToNetworkEvents() {
        mainViewModel.networkState.observe(this, state -> {
            if (state == NetworkInfo.State.CONNECTED) {
                viewModel.onInternetEvent(true);
            }
            else {
                AndroidUtils.showSnackBarMessage(root, R.string.not_full_categories_message);
            }
        });
    }

    private void clearSearchFocus() {
        if (searchView != null) {
            searchView.clearFocus();
        }
    }
}
