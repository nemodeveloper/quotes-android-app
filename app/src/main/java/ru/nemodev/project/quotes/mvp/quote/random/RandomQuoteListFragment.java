package ru.nemodev.project.quotes.mvp.quote.random;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.base.BaseQuoteAdapter;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.mvp.base.RandomQuoteRV;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class RandomQuoteListFragment extends BaseToolbarFragment implements RandomQuoteListContract.RandomQuoteListView
{
    private View root;
    private RandomQuoteRV<? extends BaseQuoteAdapter.BaseQuoteViewHolder> quoteRV;
    private ProgressBar progressBar;
    private TextView notFullContentMessage;

    private RandomQuoteListContract.RandomQuoteListPresenter presenter;

    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.random_quote_fragmet, container, false);

        initToolbar(root);
        initRV();
        initProgressBar();
        initNotFullContentMessageBlock();

        presenter = new RandomQuoteListPresenterImpl(this);
        loadNextQuotes();

        connectToNetworkEvents();

        return root;
    }

    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
        toolbar.setTitle(AndroidUtils.getTextById(R.string.random_title));
    }

    private void initRV()
    {
        quoteRV = root.findViewById(R.id.quoteList);
        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        quoteRV.setAdapter(new RandomQuoteListAdapter(getActivity()));

        quoteRV.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                // TODO подумать как эту логику перенести в Presenter
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

    private void initProgressBar()
    {
        progressBar = root.findViewById(R.id.contentLoadingProgressBar);
    }

    // TODO перейти на SnackBar
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
                        loadNextQuotes();
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

    private void setVisibleNotFullContentMessage(boolean isVisible)
    {
        if (isVisible)
            notFullContentMessage.setVisibility(View.VISIBLE);
        else
            notFullContentMessage.setVisibility(View.GONE);
    }
}