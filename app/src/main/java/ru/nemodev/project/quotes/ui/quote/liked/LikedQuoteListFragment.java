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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.commons.collections4.CollectionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.base.EmptyAdapterDataListener;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.ui.quote.liked.viewmodel.LikedQuoteViewModel;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class LikedQuoteListFragment extends BaseFragment implements EmptyAdapterDataListener, SwipeRefreshLayout.OnRefreshListener {
    private View root;

    @BindView(R.id.quoteList) RecyclerView quoteRV;
    @BindView(R.id.emptyLiked) TextView emptyLikedView;
    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;

    private LikedQuoteViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.liked_quote_fragment, container, false);
        ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(this).get(LikedQuoteViewModel.class);

        showLoader();
        initRV();

        initRefreshLayout();
        MetricUtils.viewEvent(MetricUtils.ViewType.LIKED_QUOTES);

        return root;
    }

    private void initRV() {
        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        LikedQuoteListAdapter adapter = new LikedQuoteListAdapter(getContext(), (MainActivity) getActivity(), this);
        quoteRV.setAdapter(adapter);
        viewModel.likedQuoteList.observe(this, quoteInfos -> {
            if (CollectionUtils.isNotEmpty(quoteInfos)) {
                showEmptyContentView(false);
                adapter.submitList(quoteInfos, this::hideLoader);
            }
            else {
                showEmptyContentView(true);
            }
            hideLoader();
        });
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        // TODO починить апдейт и сделать для всех фрагментов такое
    }

    private void showEmptyContentView(boolean show) {
        emptyLikedView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEmptyAdapterData() {
        showEmptyContentView(true);
    }
}