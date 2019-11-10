package ru.nemodev.project.quotes.ui.quote.random;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.ui.quote.random.viewmodel.RandomQuoteViewModel;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class RandomQuoteListFragment extends BaseFragment {

    @BindView(R.id.quoteList) RecyclerView quoteRV;

    private RandomQuoteViewModel viewModel;

    public RandomQuoteListFragment() {
        super(R.layout.random_quote_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(RandomQuoteViewModel.class);

        initialize();
        connectToNetworkEvents();
        MetricUtils.viewEvent(MetricUtils.ViewType.RANDOM_QUOTES);

        return root;
    }

    private void initialize() {
        showLoader();

        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        RandomQuoteListAdapter adapter = new RandomQuoteListAdapter(getActivity(), (MainActivity) getActivity());
        quoteRV.setAdapter(adapter);
        viewModel.randomQuoteList.observe(this, quoteInfos -> {
            adapter.submitList(quoteInfos, this::hideLoader);
        });
    }

    private void connectToNetworkEvents() {
        mainViewModel.networkState.observe(this, state -> {
            if (state == NetworkInfo.State.CONNECTED) {
                // TODO обновлять данные и перед этим чистить кеш так же во всех фрагментах
            }
            else {
                AndroidUtils.showSnackBarMessage(root, R.string.not_full_quotes_message);
            }
        });
    }
}