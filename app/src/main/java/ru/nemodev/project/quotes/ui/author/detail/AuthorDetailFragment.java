package ru.nemodev.project.quotes.ui.author.detail;

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
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.author.detail.viewmodel.QuoteByAuthorViewModel;
import ru.nemodev.project.quotes.ui.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class AuthorDetailFragment extends BaseToolbarFragment {
    public static final String AUTHOR_ID_KEY = "authorId";
    public static final String AUTHOR_NAME_KEY = "authorName";

    private View root;

    @BindView(R.id.quoteList) RecyclerView quoteRV;

    private QuoteByAuthorViewModel viewModel;
    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.base_quote_fragmet, container, false);
        ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(this).get(QuoteByAuthorViewModel.class);

        showLoader();
        initToolbar();
        initRV();
        connectToNetworkEvents();

        return root;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        toolbar.setTitle(getArguments().getString(AUTHOR_NAME_KEY));
    }

    private void initRV() {
        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        QuoteByAuthorAdapter adapter = new QuoteByAuthorAdapter(getContext(), (MainActivity) getActivity());
        quoteRV.setAdapter(adapter);
        viewModel.getQuoteByAuthorList(getArguments().getLong(AUTHOR_ID_KEY))
                .observe(this, quoteInfos -> {
                    adapter.submitList(quoteInfos, this::hideLoader);
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
