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
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.author.detail.viewmodel.QuoteByAuthorViewModel;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class AuthorDetailFragment extends BaseFragment {

    @BindView(R.id.quoteList) RecyclerView quoteRV;

    private QuoteByAuthorViewModel viewModel;

    public AuthorDetailFragment() {
        super(R.layout.base_quote_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(QuoteByAuthorViewModel.class);

        initialize();
        connectToNetworkEvents();

        return root;
    }

    private void initialize() {
        showLoader();

        AuthorDetailFragmentArgs args = AuthorDetailFragmentArgs.fromBundle(getArguments());

        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        QuoteByAuthorAdapter adapter = new QuoteByAuthorAdapter(getContext(), (MainActivity) getActivity());
        quoteRV.setAdapter(adapter);
        viewModel.getQuoteByAuthorList(args.getAuthorId())
                .observe(this, quoteInfos -> adapter.submitList(quoteInfos, this::hideLoader));
    }

    private void connectToNetworkEvents() {
        mainViewModel.networkState.observe(this, state -> {
            if (state == NetworkInfo.State.CONNECTED) {
            }
            else {
                AndroidUtils.showSnackBarMessage(root, R.string.not_full_quotes_message);
            }
        });
    }
}
