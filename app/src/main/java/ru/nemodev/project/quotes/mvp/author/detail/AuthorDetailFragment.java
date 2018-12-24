package ru.nemodev.project.quotes.mvp.author.detail;

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
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class AuthorDetailFragment extends BaseToolbarFragment implements AuthorDetailContract.AuthorDetailView
{
    public static final String AUTHOR_ID_KEY = "authorId";
    public static final String AUTHOR_NAME_KEY = "authorName";

    private View root;
    private RecyclerView quoteRV;
    private ProgressBar progressBar;
    private TextView notFullContentMessage;

    private AuthorDetailContract.AuthorDetailPresenter presenter;

    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.base_quote_fragmet, container, false);

        initToolbar(root);
        initRV();
        initProgressBar();
        initNotFullContentMessageBlock();

        presenter = new AuthorDetailPresenterImpl(getArguments().getLong(AUTHOR_ID_KEY), this);
        presenter.loadQuotes();

        connectToNetworkEvents();

        return root;
    }

    // TODO переписать чтобы эту логику делал presenter
    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
        toolbar.setTitle(getArguments().getString(AUTHOR_NAME_KEY));
    }

    private void initRV()
    {
        quoteRV = root.findViewById(R.id.quoteList);
        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initProgressBar()
    {
        progressBar = root.findViewById(R.id.contentLoadingProgressBar);
    }

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
                        presenter.loadQuotes();
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
    public void showQuotes(List<QuoteInfo> quotes)
    {
        // TODO прокидывать OnClickQuoteActionListener вместо Context
        if (CollectionUtils.isNotEmpty(quotes))
            quoteRV.setAdapter(new AuthorQuotesAdapter(getActivity(), quotes));
    }

    private void setVisibleNotFullContentMessage(boolean isVisible)
    {
        if (isVisible)
            notFullContentMessage.setVisibility(View.VISIBLE);
        else
            notFullContentMessage.setVisibility(View.GONE);
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
