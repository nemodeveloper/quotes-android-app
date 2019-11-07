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
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.author.list.viewmodel.AuthorListViewModel;
import ru.nemodev.project.quotes.ui.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class AuthorListFragment extends BaseToolbarFragment {
    private View root;

    @BindView(R.id.authorList) RecyclerView authorLoadRV;

    private SearchView searchView;
    private String whatSearch;

    private AuthorListViewModel viewModel;
    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.author_fragmet, container, false);
        ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(getActivity()).get(AuthorListViewModel.class);

        initToolbar();
        initRV();

        connectToNetworkEvents();

        MetricUtils.viewEvent(MetricUtils.ViewType.AUTHOR_LIST);

        return root;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        toolbar.setTitle(AndroidUtils.getString(R.string.author_title));
        setHasOptionsMenu(true);
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

        viewModel.getAuthorList(this, whatSearch).observe(this, authors -> {
            ((AuthorListAdapter) authorLoadRV.getAdapter()).submitList(authors, this::hideLoader);
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
        authorLoadRV.setHasFixedSize(true);
        authorLoadRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        AuthorListAdapter adapter = new AuthorListAdapter(getContext(), item -> {
            clearSearchFocus(); // TODO если вернуться обратно показываются результаты поиска без строки поиска
            MetricUtils.viewEvent(MetricUtils.ViewType.AUTHOR_QUOTES_FROM_MENU);
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.openQuoteFragment(item);
        });

        authorLoadRV.setAdapter(adapter);
        searchAuthor(whatSearch);

        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    private void connectToNetworkEvents() {
        disconnectFromNetworkEvents();
        internetEventsDisposable = NetworkUtils.getNetworkObservable()
            .subscribe(connectivity -> {
                if (connectivity.state() == NetworkInfo.State.CONNECTED) {
                    // TODO разобраться как обновлять
                }
                else {
                    AndroidUtils.showSnackBarMessage(root, R.string.not_full_authors_message);
                }
            });
    }

    private void disconnectFromNetworkEvents() {
        if (internetEventsDisposable != null && !internetEventsDisposable.isDisposed())
            internetEventsDisposable.dispose();
    }

    private void clearSearchFocus() {
        if (searchView != null)
        {
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
