package ru.nemodev.project.quotes.mvp.purchase;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.Purchase;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class PurchaseListPresenterImpl implements PurchaseListContract.PurchaseInAppListPresenter
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
    public void loadPurchaseList()
    {
        skuInAppListView.showLoader();

        purchaseModel.loadPurchaseInAppList(AndroidUtils.getStringList(R.array.inapp_products))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Purchase>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<Purchase> purchaseList)
                    {
                        skuInAppListView.showPurchaseList(purchaseList);
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
    public void onPurchaseClick(Purchase purchase)
    {
        if (purchase.isPurchase())
        {
            skuInAppListView.showMessage(
                    String.format(
                            AndroidUtils.getString(R.string.purchase_already_do_detail),
                            purchase.getTitle()));
        }
        else
        {
            purchaseModel.purchase(purchase.getPurchaseType());
        }
    }
}
