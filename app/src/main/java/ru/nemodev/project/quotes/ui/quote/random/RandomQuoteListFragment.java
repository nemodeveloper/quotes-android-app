package ru.nemodev.project.quotes.ui.quote.random;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.ui.quote.random.viewmodel.RandomQuoteViewModel;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class RandomQuoteListFragment extends BaseToolbarFragment {

    private View root;

    @BindView(R.id.quoteList) RecyclerView quoteRV;
    @BindView(R.id.contentLoadingProgressBar) ProgressBar progressBar;

    private RandomQuoteViewModel viewModel;
    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.random_quote_fragmet, container, false);
        ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(getActivity()).get(RandomQuoteViewModel.class);

        showLoader();
        initToolbar();
        initRV();
        connectToNetworkEvents();

        MetricUtils.viewEvent(MetricUtils.ViewType.RANDOM_QUOTES);

        return root;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        toolbar.setTitle(AndroidUtils.getString(R.string.random_title));
    }

    private void initRV() {
        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        RandomQuoteListAdapter adapter = new RandomQuoteListAdapter(getActivity(), (MainActivity) getActivity());
        quoteRV.setAdapter(adapter);
        viewModel.randomQuoteList.observe(this, quoteInfos -> {
            adapter.submitList(quoteInfos);
            hideLoader();
        });
    }

    private void connectToNetworkEvents() {
        disconnectFromNetworkEvents();
        internetEventsDisposable = NetworkUtils.getNetworkObservable()
                .subscribe(connectivity -> {
                    if (connectivity.state() == NetworkInfo.State.CONNECTED) {

                    }
                    else {
                        AndroidUtils.showSnackBarMessage(root, R.string.not_full_quotes_message);
                    }
                });
    }

    private void disconnectFromNetworkEvents() {
        if (internetEventsDisposable != null && !internetEventsDisposable.isDisposed())
            internetEventsDisposable.dispose();
    }

    public void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoader() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        disconnectFromNetworkEvents();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        disconnectFromNetworkEvents();
        super.onPause();
    }

    @Override
    public void onResume() {
        connectToNetworkEvents();
        super.onResume();
    }
}