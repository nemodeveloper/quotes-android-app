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
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.databinding.CategoryListFragmentBinding;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.base.observable.RxSearchObservable;
import ru.nemodev.project.quotes.ui.category.list.viewmodel.CategoryListViewModel;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.utils.AnalyticUtils;
import ru.nemodev.project.quotes.utils.AndroidUtils;


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
        AnalyticUtils.viewEvent(AnalyticUtils.ViewType.CATEGORY_LIST);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_category_bar, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(AndroidUtils.getString(R.string.category_search_hint));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        LiveDataReactiveStreams.fromPublisher(
            RxSearchObservable.fromView(searchView)
                    .debounce(1000, TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .subscribeOn(Schedulers.io())
                    .toFlowable(BackpressureStrategy.BUFFER))
            .observe(this, this::searchCategory);

        viewModel.searchString.observe(this, s -> searchView.setQuery(s, false));
    }

    private void searchCategory(String search) {
        if (StringUtils.isNotEmpty(search)) {
            AnalyticUtils.searchEvent(AnalyticUtils.SearchType.AUTHOR, search);
        }

        CategoryListAdapter adapter = (CategoryListAdapter) binding.categoryList.getAdapter();
        viewModel.getCategoryList(this, search).observe(this,
                categories-> adapter.submitList(categories, () -> {
                    binding.categoryList.scrollToPosition(0);
                    hideLoader();
                }));
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

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getName());
            AnalyticUtils.viewEvent(AnalyticUtils.ViewType.CATEGORY_QUOTES_FROM_MENU, bundle);

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

    @Override
    public void onPause() {
        clearSearchFocus();
        super.onPause();
    }
}
