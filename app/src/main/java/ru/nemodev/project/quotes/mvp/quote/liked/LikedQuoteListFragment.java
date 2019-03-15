package ru.nemodev.project.quotes.mvp.quote.liked;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.MainActivity;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class LikedQuoteListFragment extends BaseToolbarFragment implements LikedQuoteListContract.LikedQuoteListView
{
    private View root;
    private RecyclerView quoteRV;
    private ProgressBar progressBar;

    private LikedQuoteListContract.LikedQuoteListPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.liked_quote_fragmet, container, false);

        initToolbar(root);
        initProgressBar();
        initRV();

        presenter = new LikedQuoteListPresenterImpl(this);
        presenter.loadLikedQuotes();

        return root;
    }

    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
        toolbar.setTitle(AndroidUtils.getTextById(R.string.like_title));
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
            quoteRV.setAdapter(new LikedQuoteListAdapter(getActivity(), quotes, (MainActivity) getActivity()));
        }
    }
}