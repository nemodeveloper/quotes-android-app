package ru.nemodev.project.quotes.mvp.purchase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.anjlab.android.iab.v3.SkuDetails;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.mvp.main.MainContract;
import ru.nemodev.project.quotes.utils.AndroidUtils;

public class PurchaseListFragment extends BaseToolbarFragment implements PurchaseListContract.SkuInAppListView
{
    private View root;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.skuList)
    RecyclerView skuRV;
    @BindView(R.id.contentLoadingProgressBar)
    ProgressBar progressBar;

    private MainContract.MainPresenter mainPresenter;
    private PurchaseListContract.SkuInAppListPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.purchase_list_fragmet, container, false);
        ButterKnife.bind(this, root);

        initToolbar(root);
        initRV();

        presenter = new PurchaseListPresenterImpl(mainPresenter.getPurchaseModel(), this);
        presenter.loadSkuList();

        return root;
    }

    public void setMainPresenter(MainContract.MainPresenter mainPresenter)
    {
        this.mainPresenter = mainPresenter;
    }

    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
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
    public void showSkuList(List<SkuDetails> skuDetailsList)
    {
        if (CollectionUtils.isNotEmpty(skuDetailsList))
        {
            skuRV.setAdapter(new PurchaseAdapter(this.getActivity(), skuDetailsList,
                    skuDetails -> presenter.onSkuClick(skuDetails)));
        }
    }
}
