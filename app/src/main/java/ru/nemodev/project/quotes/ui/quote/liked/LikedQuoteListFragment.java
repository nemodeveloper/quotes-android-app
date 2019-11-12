package ru.nemodev.project.quotes.ui.quote.liked;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.collections4.CollectionUtils;

import butterknife.BindView;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.ui.quote.liked.viewmodel.LikedQuoteViewModel;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class LikedQuoteListFragment extends BaseFragment {

    @BindView(R.id.quoteList) RecyclerView quoteRV;
    @BindView(R.id.emptyLiked) TextView emptyLikedView;

    private LikedQuoteViewModel viewModel;

    public LikedQuoteListFragment() {
        super(R.layout.liked_quote_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(LikedQuoteViewModel.class);
        initialize();
        MetricUtils.viewEvent(MetricUtils.ViewType.LIKED_QUOTES);

        return root;
    }

    private void initialize() {
        showLoader();

        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        LikedQuoteListAdapter adapter = new LikedQuoteListAdapter(getContext(), (MainActivity) getActivity());
        quoteRV.setAdapter(adapter);
        viewModel.likedQuoteList.observe(this, quoteInfos -> {
            if (CollectionUtils.isNotEmpty(quoteInfos)) {
                showEmptyContentView(false);
            }
            else {
                showEmptyContentView(true);
                hideLoader();
            }
            adapter.submitList(quoteInfos, this::hideLoader);
        });
    }

    private void showEmptyContentView(boolean show) {
        emptyLikedView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}