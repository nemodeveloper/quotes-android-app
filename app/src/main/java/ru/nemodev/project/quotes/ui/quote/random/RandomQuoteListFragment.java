package ru.nemodev.project.quotes.ui.quote.random;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.databinding.QuoteListFragmentBinding;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.ui.quote.random.viewmodel.RandomQuoteViewModel;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class RandomQuoteListFragment extends BaseFragment {

    private QuoteListFragmentBinding binding;
    private RandomQuoteViewModel viewModel;

    public RandomQuoteListFragment() {
        super(R.layout.quote_list_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(RandomQuoteViewModel.class);
        binding = DataBindingUtil.bind(root);

        initialize();
        connectToNetworkEvents();
        MetricUtils.viewEvent(MetricUtils.ViewType.RANDOM_QUOTES);

        return root;
    }

    private void initialize() {
        showLoader();

        binding.quoteList.setHasFixedSize(true);
        binding.quoteList.setLayoutManager(new LinearLayoutManager(getActivity()));

        RandomQuoteListAdapter adapter = new RandomQuoteListAdapter(getActivity(), (MainActivity) getActivity());
        binding.quoteList.setAdapter(adapter);
        viewModel.randomQuoteList.observe(this,
                quoteInfos -> adapter.submitList(quoteInfos, () -> {
                    binding.quoteList.scrollToPosition(0);
                    binding.swipeRefresh.setRefreshing(false);
                    hideLoader();
                }));

        binding.swipeRefresh.setOnRefreshListener(() -> {
            showLoader();
            viewModel.refresh();
        });
    }

    private void connectToNetworkEvents() {
        mainViewModel.networkState.observe(this, state -> {
            if (state == NetworkInfo.State.CONNECTED) {
                viewModel.onInternetEvent(true);
            }
            else {
                AndroidUtils.showSnackBarMessage(root, R.string.not_full_quotes_message);
            }
        });
    }
}