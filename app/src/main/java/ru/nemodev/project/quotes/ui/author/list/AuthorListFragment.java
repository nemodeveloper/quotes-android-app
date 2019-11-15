package ru.nemodev.project.quotes.ui.author.list;

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
import ru.nemodev.project.quotes.databinding.AuthorListFragmentBinding;
import ru.nemodev.project.quotes.ui.author.list.viewmodel.AuthorListViewModel;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class AuthorListFragment extends BaseFragment {

    private SearchView searchView;
    private AuthorListViewModel viewModel;
    private AuthorListFragmentBinding binding;

    public AuthorListFragment() {
        super(R.layout.author_list_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(AuthorListViewModel.class);
        binding = DataBindingUtil.bind(root);

        setHasOptionsMenu(true);
        initialize();

        connectToNetworkEvents();
        MetricUtils.viewEvent(MetricUtils.ViewType.AUTHOR_LIST);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_author_bar, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(AndroidUtils.getString(R.string.author_search_hint));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAuthor(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchAuthor(query);
                return false;
            }
        });

    }

    private void searchAuthor(String search) {
        showLoader();

        if (StringUtils.isNotEmpty(search)) {
            MetricUtils.searchEvent(MetricUtils.SearchType.AUTHOR, search);
        }

        viewModel.getAuthorList(this, search).observe(this,
                authors -> ((AuthorListAdapter) binding.authorList.getAdapter()).submitList(authors, this::hideLoader));
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

        binding.authorList.setHasFixedSize(true);
        binding.authorList.setLayoutManager(new LinearLayoutManager(getActivity()));

        AuthorListAdapter adapter = new AuthorListAdapter(getContext(), item -> {
            clearSearchFocus(); // TODO если вернуться обратно показываются результаты поиска без строки поиска
            MetricUtils.viewEvent(MetricUtils.ViewType.AUTHOR_QUOTES_FROM_MENU);
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.openQuoteFragment(item);
        });

        binding.authorList.setAdapter(adapter);
        searchAuthor(null);

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
                AndroidUtils.showSnackBarMessage(root, R.string.not_full_authors_message);
            }
        });
    }

    private void clearSearchFocus() {
        if (searchView != null)
        {
            searchView.clearFocus();
        }
    }
}
