package ru.nemodev.project.quotes.mvp.purchase;

import com.anjlab.android.iab.v3.SkuDetails;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class PurchaseListPresenterImpl implements PurchaseListContract.SkuInAppListPresenter
{
    private final PurchaseListContract.SkuInAppListView skuInAppListView;
    private final PurchaseModel purchaseModel;

    public PurchaseListPresenterImpl(PurchaseModel purchaseModel,
                                     PurchaseListContract.SkuInAppListView skuInAppListView)
    {
        this.purchaseModel = purchaseModel;
        this.skuInAppListView = skuInAppListView;
    }

    @Override
    public void loadSkuList()
    {
        skuInAppListView.showLoader();

        purchaseModel.loadSkuInAppList(AndroidUtils.getStringList(R.array.inapp_products))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SkuDetails>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<SkuDetails> skuDetails)
                    {
                        skuInAppListView.showSkuList(skuDetails);
                        skuInAppListView.hideLoader();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        skuInAppListView.hideLoader();
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void onSkuClick(SkuDetails skuDetails)
    {
        purchaseModel.purchase(PurchaseType.getBySkuName(skuDetails.productId));
    }
}
