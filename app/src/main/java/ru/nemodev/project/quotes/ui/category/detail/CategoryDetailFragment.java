package ru.nemodev.project.quotes.ui.category.detail;

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
import ru.nemodev.project.quotes.ui.category.detail.viewmodel.QuoteByCategoryViewModel;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class CategoryDetailFragment extends BaseFragment {

    @BindView(R.id.quoteList) RecyclerView quoteRV;

    private QuoteByCategoryViewModel viewModel;

    public CategoryDetailFragment() {
        super(R.layout.base_quote_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(QuoteByCategoryViewModel.class);

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

        CategoryDetailFragmentArgs args = CategoryDetailFragmentArgs.fromBundle(getArguments());

        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        QuoteByCategoryAdapter adapter = new QuoteByCategoryAdapter(getContext(), (MainActivity) getActivity());
        quoteRV.setAdapter(adapter);
        viewModel.getQuoteByCategoryList(args.getCategoryId())
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