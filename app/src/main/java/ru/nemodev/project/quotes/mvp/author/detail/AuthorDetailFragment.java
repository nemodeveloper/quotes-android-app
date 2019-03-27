package ru.nemodev.project.quotes.mvp.author.detail;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.mvp.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class AuthorDetailFragment extends BaseToolbarFragment implements AuthorDetailContract.AuthorDetailView
{
    public static final String AUTHOR_ID_KEY = "authorId";
    public static final String AUTHOR_NAME_KEY = "authorName";

    private View root;

    @BindView(R.id.quoteList)
    RecyclerView quoteRV;

    @BindView(R.id.contentLoadingProgressBar)
    ProgressBar progressBar;

    private AuthorDetailContract.AuthorDetailPresenter presenter;

    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.base_quote_fragmet, container, false);
        ButterKnife.bind(this, root);

        initToolbar();
        initRV();

        presenter = new AuthorDetailPresenterImpl(getArguments().getLong(AUTHOR_ID_KEY), this);
        presenter.loadQuotes();

        connectToNetworkEvents();

        return root;
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar();
        toolbar.setTitle(getArguments().getString(AUTHOR_NAME_KEY));
    }

    private void initRV()
    {
        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                    }
                    else
                    {
                        AndroidUtils.showSnackBarMessage(root, R.string.not_full_quotes_message);
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
        if (CollectionUtils.isNotEmpty(quotes))
            quoteRV.setAdapter(new AuthorQuotesAdapter(getActivity(), quotes, (MainActivity) getActivity()));
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
