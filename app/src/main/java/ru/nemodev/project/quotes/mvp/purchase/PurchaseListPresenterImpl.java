package ru.nemodev.project.quotes.mvp.purchase;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.purchase.Purchase;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public class PurchaseListPresenterImpl implements PurchaseListContract.PurchaseInAppListPresenter
{
    private final PurchaseListContract.SkuInAppListView skuInAppListView;
    private final PurchaseInteractor purchaseInteractor;

    public PurchaseListPresenterImpl(PurchaseInteractor purchaseInteractor,
                                     PurchaseListContract.SkuInAppListView skuInAppListView)
    {
        this.purchaseInteractor = purchaseInteractor;
        this.skuInAppListView = skuInAppListView;
    }

    @Override
    public void loadPurchaseList()
    {
        skuInAppListView.showLoader();

        purchaseInteractor.loadPurchaseInAppList(AndroidUtils.getStringList(R.array.inapp_products))
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
            purchaseInteractor.purchase(purchase.getPurchaseType());
        }
    }

    @Override
    public void onPurchase(String productId)
    {
        purchaseInteractor.loadPurchase(productId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(purchase -> loadPurchaseList());
    }
}
