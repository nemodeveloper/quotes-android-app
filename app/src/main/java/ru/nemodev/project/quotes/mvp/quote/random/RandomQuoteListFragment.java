package ru.nemodev.project.quotes.mvp.quote.random;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.mvp.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class RandomQuoteListFragment extends BaseToolbarFragment implements RandomQuoteListContract.RandomQuoteListView
{
    private View root;

    @BindView(R.id.quoteList)
    RandomQuoteRV<? extends BaseQuoteAdapter.BaseQuoteViewHolder> quoteRV;

    @BindView(R.id.contentLoadingProgressBar)
    ProgressBar progressBar;

    private RandomQuoteListContract.RandomQuoteListPresenter presenter;

    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.random_quote_fragmet, container, false);
        ButterKnife.bind(this, root);

        initToolbar();
        initRV();

        presenter = new RandomQuoteListPresenterImpl(this);
        loadNextQuotes();

        connectToNetworkEvents();

        MetricUtils.viewEvent(MetricUtils.ViewType.RANDOM_QUOTES);

        return root;
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar();
        toolbar.setTitle(AndroidUtils.getString(R.string.random_title));
    }

    private void initRV()
    {
        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        quoteRV.setAdapter(new RandomQuoteListAdapter(getActivity(), (MainActivity) getActivity()));

        quoteRV.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                loadNextQuotes();
            }
        });
    }

    private void loadNextQuotes()
    {
        if (quoteRV.isNeedLoadNewData())
        {
            quoteRV.setDataLoading(true);
            presenter.loadNextQuotes();
        }
    }

    private void connectToNetworkEvents()
    {
        disconnectFromNetworkEvents();
        internetEventsDisposable = NetworkUtils.getNetworkObservable()
                .subscribe(connectivity ->
                {
                    if (connectivity.state() == NetworkInfo.State.CONNECTED)
                    {
                        loadNextQuotes();
                    }
                    else
                    {
                        AndroidUtils.showSnackBarMessage(
                                getActivity().findViewById(R.id.viewContainer),
                                R.string.not_full_quotes_message);
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
    public void showNextQuotes(List<QuoteInfo> quotes)
    {
        if (CollectionUtils.isNotEmpty(quotes))
        {
            quoteRV.getAdapter().addItems(quotes);
            quoteRV.getAdapter().notifyItemInserted(quoteRV.getAdapter().getItemCount() - quotes.size());
        }

        quoteRV.setDataLoading(false);
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