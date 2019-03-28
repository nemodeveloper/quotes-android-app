package ru.nemodev.project.quotes.mvp.purchase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.Purchase;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.mvp.main.MainContract;
import ru.nemodev.project.quotes.utils.AndroidUtils;

public class PurchaseListFragment extends BaseToolbarFragment implements PurchaseListContract.SkuInAppListView
{
    private View root;

    @BindView(R.id.skuList)
    RecyclerView skuRV;
    @BindView(R.id.contentLoadingProgressBar)
    ProgressBar progressBar;
    @BindView(R.id.purchaseEmptyView)
    TextView purchaseEmptyView;

    private MainContract.MainPresenter mainPresenter;
    private PurchaseListContract.PurchaseInAppListPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.purchase_list_fragmet, container, false);
        ButterKnife.bind(this, root);

        initToolbar();
        initRV();

        presenter = new PurchaseListPresenterImpl(mainPresenter.getPurchaseModel(), this);
        mainPresenter.setBillingEventListener(presenter);
        presenter.loadPurchaseList();

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

    @Override
    protected void initToolbar()
    {
        super.initToolbar();
        this.toolbar.setTitle(AndroidUtils.getString(R.string.menu_item_purchase));
    }

    private void initRV()
    {
        skuRV.setHasFixedSize(true);
        skuRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showLoader()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader()
    {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showPurchaseList(List<Purchase> purchaseList)
    {
        if (CollectionUtils.isNotEmpty(purchaseList))
        {
            showEmptyContentView(false);
            skuRV.setAdapter(new PurchaseAdapter(this.getActivity(), purchaseList,
                    purchase -> presenter.onPurchaseClick(purchase)));
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
}
