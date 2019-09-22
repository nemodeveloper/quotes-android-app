package ru.nemodev.project.quotes.mvp.quote.liked;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.mvp.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class LikedQuoteListFragment extends BaseToolbarFragment implements LikedQuoteListContract.LikedQuoteListView, SwipeRefreshLayout.OnRefreshListener
{
    private View root;

    @BindView(R.id.quoteList)
    RecyclerView quoteRV;

    @BindView(R.id.contentLoadingProgressBar)
    ProgressBar progressBar;

    @BindView(R.id.emptyLiked)
    TextView emptyLikedView;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    private LikedQuoteListContract.LikedQuoteListPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.liked_quote_fragmet, container, false);
        ButterKnife.bind(this, root);

        initToolbar();
        initRV();

        presenter = new LikedQuoteListPresenterImpl(this);
        presenter.loadLikedQuotes();

        initRefreshLayout();

        MetricUtils.viewEvent(MetricUtils.ViewType.LIKED_QUOTES);

        return root;
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar();
        toolbar.setTitle(AndroidUtils.getString(R.string.like_title));
    }

    private void initRV()
    {
        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initRefreshLayout()
    {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh()
    {
        presenter.loadLikedQuotes();
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
    public void showLikedQuotes(List<QuoteInfo> quotes)
    {
        if (CollectionUtils.isNotEmpty(quotes))
        {
            showEmptyContentView(false);
            quoteRV.setAdapter(new LikedQuoteListAdapter(getActivity(), quotes,
                    (MainActivity) getActivity(),
                    () -> showEmptyContentView(true)));
        }
        else
        {
            showEmptyContentView(true);
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    private void showEmptyContentView(boolean show)
    {
        emptyLikedView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}