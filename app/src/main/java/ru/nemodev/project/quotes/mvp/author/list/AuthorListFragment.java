package ru.nemodev.project.quotes.mvp.author.list;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.nemodev.core.utils.AndroidUtils;
import ru.nemodev.core.utils.NetworkUtils;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.mvp.main.MainActivity;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class AuthorListFragment extends BaseToolbarFragment implements AuthorListContract.AuthorListView
{
    private View root;

    @BindView(R.id.authorList)
    RecyclerView authorLoadRV;

    @BindView(R.id.contentLoadingProgressBar)
    ProgressBar progressBar;

    private SearchView searchView;
    private String whatSearch;

    private AuthorListContract.AuthorListPresenter presenter;

    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.author_fragmet, container, false);
        ButterKnife.bind(this, root);

        initToolbar();
        initRV(root);

        presenter = new AuthorListPresenterImpl(this);
        presenter.loadAuthors();

        connectToNetworkEvents();

        MetricUtils.viewEvent(MetricUtils.ViewType.AUTHOR_LIST);

        return root;
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar();
        toolbar.setTitle(AndroidUtils.getString(R.string.author_title));

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
            inflater.inflate(R.menu.menu_author_bar, menu);

            searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setQueryHint(AndroidUtils.getString(R.string.author_search_hint));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
            {
                @Override
                public boolean onQueryTextSubmit(String query)
                {
                    searchAuthor(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query)
                {
                    searchAuthor(query);
                    return false;
                }
            });
            searchView.setOnSearchClickListener(v ->
            {
                AuthorRVAdapter adapter = (AuthorRVAdapter) authorLoadRV.getAdapter();
                adapter.setAnimationEnable(false);
            });
            searchView.setOnCloseListener(() ->
            {
                AuthorRVAdapter adapter = (AuthorRVAdapter) authorLoadRV.getAdapter();
                adapter.setAnimationEnable(true);

                return false;
            });
        }
    }

    private boolean isCanSearch()
    {
        AuthorRVAdapter adapter = (AuthorRVAdapter) authorLoadRV.getAdapter();
        return adapter != null;
    }

    private void searchAuthor(String search)
    {
        if (!isCanSearch())
            return;

        if (StringUtils.isNotEmpty(search))
        {
            whatSearch = search;
        }
        else
        {
            MetricUtils.searchEvent(MetricUtils.SearchType.AUTHOR, AuthorListFragment.this.whatSearch);
        }

        AuthorRVAdapter adapter = (AuthorRVAdapter) authorLoadRV.getAdapter();
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
        authorLoadRV.setHasFixedSize(true);
        authorLoadRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void connectToNetworkEvents()
    {
        disconnectFromNetworkEvents();
        internetEventsDisposable = NetworkUtils.getNetworkObservable()
                .subscribe(connectivity ->
                {
                    if (connectivity.state() == NetworkInfo.State.CONNECTED)
                    {
                        presenter.loadAuthors();
                    }
                    else
                    {
                        AndroidUtils.showSnackBarMessage(
                                getActivity().findViewById(R.id.viewContainer),
                                R.string.not_full_authors_message);
                    }
                });
    }

    private void disconnectFromNetworkEvents()
    {
        if (internetEventsDisposable != null && !internetEventsDisposable.isDisposed())
            internetEventsDisposable.dispose();
    }

    private void clearSearchFocus()
    {
        if (searchView != null)
        {
            searchView.clearFocus();
        }
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
    public void showAuthors(List<Author> authors)
    {
        if (CollectionUtils.isNotEmpty(authors))
        {
            authorLoadRV.setAdapter(new AuthorRVAdapter(getActivity(), authors, item ->
            {
                clearSearchFocus();
                MetricUtils.viewEvent(MetricUtils.ViewType.AUTHOR_QUOTES_FROM_MENU);
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.openQuoteFragment(item);
            }));

            if (getActivity() != null)
            {
                getActivity().invalidateOptionsMenu();
            }
        }
    }

    @Override
    public void onDestroyOptionsMenu()
    {
        clearSearchFocus();
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
