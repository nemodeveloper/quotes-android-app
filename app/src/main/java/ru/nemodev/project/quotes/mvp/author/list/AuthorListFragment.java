package ru.nemodev.project.quotes.mvp.author.list;

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
import ru.nemodev.project.quotes.entity.Author;
import ru.nemodev.project.quotes.mvp.MainActivity;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class AuthorListFragment extends BaseToolbarFragment implements AuthorListContract.AuthorListView
{
    private View root;
    private IndexFastScrollRecyclerView authorLoadRV;
    private ProgressBar progressBar;
    private TextView notFullContentMessage;
    private SearchView searchView;

    private AuthorListContract.AuthorListPresenter presenter;

    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.author_fragmet, container, false);

        initToolbar(root);
        initRV(root);
        initProgressBar();
        initNotFullContentMessageBlock();

        presenter = new AuthorListPresenterImpl(this);
        presenter.loadAuthors();

        connectToNetworkEvents();

        return root;
    }

    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
        toolbar.setTitle(AndroidUtils.getTextById(R.string.author_title));

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
            searchView.setQueryHint(AndroidUtils.getTextById(R.string.author_search_hint));
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
                authorLoadRV.setIndexBarVisibility(false);

                AuthorRVAdapter adapter = (AuthorRVAdapter) authorLoadRV.getAdapter();
                adapter.setAnimationEnable(false);
            });
            searchView.setOnCloseListener(() ->
            {
                authorLoadRV.setIndexBarVisibility(true);

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
        authorLoadRV = root.findViewById(R.id.authorList);
        authorLoadRV.setHasFixedSize(true);
        authorLoadRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        authorLoadRV.setIndexBarVisibility(false);
        authorLoadRV.setIndexBarCornerRadius(0);
        authorLoadRV.setIndexBarTransparentValue(0.5f);
        authorLoadRV.setPreviewPadding(0);
        authorLoadRV.setIndexbarMargin(0);
        authorLoadRV.setIndexBarColor(R.color.search_bar_background);
    }

    private void initProgressBar()
    {
        progressBar = root.findViewById(R.id.contentLoadingProgressBar);
    }

    // TODO полумать как обыграть события сети более красиво в рамках MVP
    private void connectToNetworkEvents()
    {
        disconnectFromNetworkEvents();
        internetEventsDisposable = NetworkUtils.getNetworkObservable()
                .subscribe(connectivity ->
                {
                    if (connectivity.state() == NetworkInfo.State.CONNECTED)
                    {
                        presenter.loadAuthors();
                        setVisibleNotFullContentMessage(false);
                    }
                    else
                    {
                        setVisibleNotFullContentMessage(true);
                    }
                });
    }

    private void initNotFullContentMessageBlock()
    {
        notFullContentMessage = root.findViewById(R.id.not_full_content_message);
        notFullContentMessage.setOnClickListener(view -> setVisibleNotFullContentMessage(false));
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
    public void showAuthors(List<Author> authors)
    {
        if (CollectionUtils.isNotEmpty(authors))
        {
            authorLoadRV.setAdapter(new AuthorRVAdapter(getActivity(), authors, item ->
            {
                searchView.clearFocus();
                MainActivity mainActivity1 = (MainActivity) getActivity();
                mainActivity1.openQuoteFragment(item);
            }));
            authorLoadRV.setIndexBarVisibility(true);
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
