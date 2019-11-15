package ru.nemodev.project.quotes.ui.author.detail;

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
import ru.nemodev.project.quotes.ui.author.detail.viewmodel.QuoteByAuthorViewModel;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class AuthorDetailFragment extends BaseFragment {

    private QuoteByAuthorViewModel viewModel;
    private QuoteListFragmentBinding binding;

    public AuthorDetailFragment() {
        super(R.layout.quote_list_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(QuoteByAuthorViewModel.class);
        binding = DataBindingUtil.bind(root);

        initialize();
        connectToNetworkEvents();

        return root;
    }

    private void initialize() {
        showLoader();

        viewModel.startWorkEvent.observe(this, aBoolean -> {
            if (aBoolean) {
                showLoader();
            }
            else {
                hideLoader();
            }
        });

        AuthorDetailFragmentArgs args = AuthorDetailFragmentArgs.fromBundle(getArguments());

        binding.quoteList.setHasFixedSize(true);
        binding.quoteList.setLayoutManager(new LinearLayoutManager(getActivity()));

        QuoteByAuthorAdapter adapter = new QuoteByAuthorAdapter(getContext(), (MainActivity) getActivity());
        binding.quoteList.setAdapter(adapter);
        viewModel.getQuoteByAuthorList(args.getAuthorId())
                .observe(this, quoteInfos -> adapter.submitList(quoteInfos, this::hideLoader));
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
