package ru.nemodev.project.quotes.ui.quote.liked;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.apache.commons.collections4.CollectionUtils;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.databinding.LikedQuoteFragmentBinding;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.ui.quote.liked.viewmodel.LikedQuoteViewModel;
import ru.nemodev.project.quotes.utils.AnalyticUtils;


public class LikedQuoteListFragment extends BaseFragment {

    private LikedQuoteViewModel viewModel;
    private LikedQuoteFragmentBinding binding;

    public LikedQuoteListFragment() {
        super(R.layout.liked_quote_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(LikedQuoteViewModel.class);
        binding = DataBindingUtil.bind(root);

        initialize();
        AnalyticUtils.viewEvent(AnalyticUtils.ViewType.LIKED_QUOTES);

        return root;
    }

    private void initialize() {
        showLoader();

        binding.quoteList.setHasFixedSize(true);
        binding.quoteList.setLayoutManager(new LinearLayoutManager(getActivity()));

        LikedQuoteListAdapter adapter = new LikedQuoteListAdapter(getContext(), (MainActivity) getActivity());
        binding.quoteList.setAdapter(adapter);
        viewModel.likedQuoteList.observe(this, quoteInfos -> {
            showEmptyContentView(CollectionUtils.isEmpty(quoteInfos));
            adapter.submitList(quoteInfos, this::hideLoader);
        });
    }

    private void showEmptyContentView(boolean show) {
        binding.emptyLikedView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}