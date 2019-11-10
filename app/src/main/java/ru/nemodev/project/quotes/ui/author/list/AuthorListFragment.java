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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.author.list.viewmodel.AuthorListViewModel;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class AuthorListFragment extends BaseFragment {

    @BindView(R.id.authorList) RecyclerView authorRV;

    private SearchView searchView;
    private String whatSearch;

    private AuthorListViewModel viewModel;

    public AuthorListFragment() {
        super(R.layout.author_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(AuthorListViewModel.class);

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
            whatSearch = search;
            MetricUtils.searchEvent(MetricUtils.SearchType.AUTHOR, AuthorListFragment.this.whatSearch);
        }

        viewModel.getAuthorList(this, whatSearch).observe(this,
                authors -> ((AuthorListAdapter) authorRV.getAdapter()).submitList(authors, this::hideLoader));
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
        authorRV.setHasFixedSize(true);
        authorRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        AuthorListAdapter adapter = new AuthorListAdapter(getContext(), item -> {
            clearSearchFocus(); // TODO если вернуться обратно показываются результаты поиска без строки поиска
            MetricUtils.viewEvent(MetricUtils.ViewType.AUTHOR_QUOTES_FROM_MENU);
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.openQuoteFragment(item);
        });

        authorRV.setAdapter(adapter);
        searchAuthor(whatSearch);

        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    private void connectToNetworkEvents() {
        mainViewModel.networkState.observe(this, state -> {
            if (state == NetworkInfo.State.CONNECTED) {
                // TODO разобраться как обновлять
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
