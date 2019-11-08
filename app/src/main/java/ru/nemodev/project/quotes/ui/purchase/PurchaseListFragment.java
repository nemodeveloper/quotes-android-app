package ru.nemodev.project.quotes.ui.purchase;

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

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.purchase.Purchase;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.purchase.viewmodel.PurchaseViewModel;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class PurchaseListFragment extends BaseFragment {

    private View root;

    @BindView(R.id.skuList) RecyclerView recyclerView;
    @BindView(R.id.purchaseEmptyView) TextView purchaseEmptyView;

    private PurchaseViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.purchase_list_fragment, container, false);
        ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(getActivity()).get(PurchaseViewModel.class);

        initialize();

        return root;
    }

    private void onPurchaseClick(Purchase purchase) {
        if (purchase.isPurchase()) {
            showMessage(String.format(
                    AndroidUtils.getString(R.string.purchase_already_do_detail), purchase.getTitle()));
        }
        else {
            viewModel.purchase(purchase);
        }
    }

    private void initialize() {
        MetricUtils.viewEvent(MetricUtils.ViewType.PURCHASE_LIST);
        showLoader();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        showEmptyContentView(false);

        PurchaseAdapter adapter = new PurchaseAdapter(getContext(), this::onPurchaseClick);
        recyclerView.setAdapter(adapter);
        viewModel.getPurchaseList(this).observe(this,
                purchases -> adapter.submitList(purchases, this::hideLoader));

        viewModel.onPurchaseEvent.observe(this, purchase ->
                viewModel.getPurchaseList(this)
                        .observe(this,
                                purchases -> adapter.submitList(purchases, this::hideLoader)));
    }

    private void showMessage(String message) {
        if (StringUtils.isNotEmpty(message)) {
            AndroidUtils.showSnackBarMessage(root, message);
        }
    }

    // TODO сделать общее view как с загрузкой для всех фрагментов
    private void showEmptyContentView(boolean show) {
        purchaseEmptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
