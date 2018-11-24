package ru.nemodev.project.quotes.view.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.load.LoadDataListener;
import ru.nemodev.project.quotes.core.load.simple.SimpleLoadRVAdapter;
import ru.nemodev.project.quotes.entity.external.Quote;

public abstract class BaseQuoteFragment extends BaseToolbarFragment
{
    protected View root;
    protected QuoteAutoLoadRV<? extends BaseQuoteAdapter.BaseQuoteViewHolder> quoteAutoLoadRV;
    protected ProgressBar contentLoadingProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.base_quote_fragmet, container, false);
        initToolbar(root);

        contentLoadingProgressBar = root.findViewById(R.id.contentLoadingProgressBar);

        quoteAutoLoadRV = root.findViewById(R.id.quoteList);
        quoteAutoLoadRV.setHasFixedSize(true);
        quoteAutoLoadRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        quoteAutoLoadRV.setAdapter(getDataAdapter());
        quoteAutoLoadRV.loadData(getQuoteLoadListener());

        return root;
    }

    protected abstract SimpleLoadRVAdapter<Quote, ? extends RecyclerView.ViewHolder> getDataAdapter();
    protected abstract LoadDataListener<Quote> getQuoteLoadListener();
}