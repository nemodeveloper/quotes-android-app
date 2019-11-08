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
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.category.detail.viewmodel.QuoteByCategoryViewModel;
import ru.nemodev.project.quotes.ui.main.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class CategoryDetailFragment extends BaseFragment {
    private View root;

    @BindView(R.id.quoteList) RecyclerView quoteRV;

    private QuoteByCategoryViewModel viewModel;
    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.base_quote_fragment, container, false);
        ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(this).get(QuoteByCategoryViewModel.class);

        showLoader();
        initRV();
        connectToNetworkEvents();

        return root;
    }

    private void initRV() {
        CategoryDetailFragmentArgs args = CategoryDetailFragmentArgs.fromBundle(getArguments());
        // TODO подумать как это лучше обыграть
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(args.getCategoryName());

        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        QuoteByCategoryAdapter adapter = new QuoteByCategoryAdapter(getContext(), (MainActivity) getActivity());
        quoteRV.setAdapter(adapter);
        viewModel.getQuoteByCategoryList(args.getCategoryId())
                .observe(this, quoteInfos -> adapter.submitList(quoteInfos, this::hideLoader));
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