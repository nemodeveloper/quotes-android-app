package ru.nemodev.project.quotes.ui.purchase;

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
import org.apache.commons.lang3.StringUtils;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.databinding.PurchaseListFragmentBinding;
import ru.nemodev.project.quotes.entity.purchase.PurchaseItem;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.purchase.viewmodel.PurchaseViewModel;
import ru.nemodev.project.quotes.utils.AnalyticUtils;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class PurchaseListFragment extends BaseFragment {

    private PurchaseViewModel viewModel;
    private PurchaseListFragmentBinding binding;

    public PurchaseListFragment() {
        super(R.layout.purchase_list_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(PurchaseViewModel.class);
        binding = DataBindingUtil.bind(root);

        initialize();

        return root;
    }

    private void onPurchaseClick(PurchaseItem purchaseItem) {
        if (purchaseItem.isPurchase()) {
            showMessage(String.format(
                    AndroidUtils.getString(R.string.purchase_already_do_detail), purchaseItem.getTitle()));
        }
        else {
            viewModel.buy(purchaseItem);
        }
    }

    private void initialize() {
        showLoader();
        AnalyticUtils.viewEvent(AnalyticUtils.ViewType.PURCHASE_LIST);

        binding.purchaseItemList.setHasFixedSize(true);
        binding.purchaseItemList.setLayoutManager(new LinearLayoutManager(getActivity()));
        showEmptyContentView(false);

        PurchaseAdapter adapter = new PurchaseAdapter(getContext(), this::onPurchaseClick);
        binding.purchaseItemList.setAdapter(adapter);
        viewModel.purchaseList.observe(this, purchases -> {
                    viewModel.checkPurchase();
                    showEmptyContentView(CollectionUtils.isEmpty(purchases));
                    adapter.submitList(purchases, this::hideLoader);
                });
    }

    private void showMessage(String message) {
        if (StringUtils.isNotEmpty(message)) {
            AndroidUtils.showSnackBarMessage(root, message);
        }
    }

    private void showEmptyContentView(boolean show) {
        binding.purchaseEmptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
