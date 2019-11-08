package ru.nemodev.project.quotes.ui.purchase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.purchase.Purchase;
import ru.nemodev.project.quotes.ui.base.BaseFragment;
import ru.nemodev.project.quotes.ui.main.MainContract;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;


public class PurchaseListFragment extends BaseFragment implements PurchaseListContract.SkuInAppListView
{
    public static final String PURCHASE_ID_ACTION = "PURCHASE_ID_ACTION";

    private View root;

    @BindView(R.id.skuList) RecyclerView skuRV;
    @BindView(R.id.purchaseEmptyView)
    TextView purchaseEmptyView;

    private MainContract.MainPresenter mainPresenter;
    private PurchaseListContract.PurchaseInAppListPresenter presenter;

    private PurchaseType purchaseTypeAction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.purchase_list_fragment, container, false);
        ButterKnife.bind(this, root);

        initRV();

        if (getArguments() != null)
        {
            String rawPurchaseType = getArguments().getString(PURCHASE_ID_ACTION);
            purchaseTypeAction = StringUtils.isEmpty(rawPurchaseType) ? null : PurchaseType.getByProductId(rawPurchaseType);
        }

        presenter = new PurchaseListPresenterImpl(mainPresenter.getPurchaseInteractor(), this);
        mainPresenter.setBillingEventListener(presenter);
        presenter.loadPurchaseList();

        MetricUtils.viewEvent(MetricUtils.ViewType.PURCHASE_LIST);

        return root;
    }

    public void setMainPresenter(MainContract.MainPresenter mainPresenter)
    {
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void onDestroy()
    {
        mainPresenter.setBillingEventListener(null);
        super.onDestroy();
    }

    private void initRV()
    {
        skuRV.setHasFixedSize(true);
        skuRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showLoader()
    {
        super.showLoader();
    }

    @Override
    public void hideLoader()
    {
        super.hideLoader();
    }

    @Override
    public void showPurchaseList(List<Purchase> purchaseList)
    {
        if (CollectionUtils.isNotEmpty(purchaseList))
        {
            showEmptyContentView(false);
            skuRV.setAdapter(new PurchaseAdapter(this.getActivity(), purchaseList,
                    purchase -> presenter.onPurchaseClick(purchase)));
            showPurchaseAction(purchaseList);
        }
        else
        {
            showEmptyContentView(true);
        }
    }

    @Override
    public void showMessage(String message)
    {
        if (StringUtils.isNotEmpty(message))
        {
            AndroidUtils.showSnackBarMessage(root, message);
        }
    }

    // TODO сделать общее view как с загрузкой для всех фрагментов
    private void showEmptyContentView(boolean show)
    {
        purchaseEmptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showPurchaseAction(List<Purchase> purchaseList)
    {
        if (purchaseTypeAction != null
                && CollectionUtils.isNotEmpty(purchaseList))
        {
            for (Purchase purchase : purchaseList)
            {
                if (purchase.getPurchaseType() == purchaseTypeAction)
                {
                    presenter.onPurchaseClick(purchase);
                    return;
                }
            }
        }
    }
}
