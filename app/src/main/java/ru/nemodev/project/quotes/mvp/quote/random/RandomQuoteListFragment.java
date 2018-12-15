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

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;

import java.util.List;

import io.reactivex.Observer;
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

    private RandomQuoteListContract.RandomQuoteListPresenter presenter;

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

    // TODO делать dispose при смене фрагмента иначе многократно приходят уведомления при отсутствии инета
    private void connectToNetworkEvents()
    {
        NetworkUtils.getNetworkObservable()
                .subscribe(new Observer<Connectivity>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Connectivity connectivity)
                    {
                        if (connectivity.state() == NetworkInfo.State.CONNECTED)
                            loadNextQuotes();
                        else
                            AndroidUtils.showToastMessage(R.string.connect_off_message);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
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
        quoteRV.getAdapter().addItems(quotes);
        quoteRV.getAdapter().notifyItemInserted(quoteRV.getAdapter().getItemCount() - quotes.size());

        quoteRV.setDataLoading(false);
    }
}