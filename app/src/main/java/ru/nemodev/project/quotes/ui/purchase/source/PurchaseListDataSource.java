package ru.nemodev.project.quotes.ui.purchase.source;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.entity.purchase.Purchase;
import ru.nemodev.project.quotes.ui.purchase.PurchaseInteractor;

public class PurchaseListDataSource extends PositionalDataSource<Purchase> {

    private final PurchaseInteractor purchaseInteractor;

    public PurchaseListDataSource(PurchaseInteractor purchaseInteractor) {
        this.purchaseInteractor = purchaseInteractor;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Purchase> callback) {

        purchaseInteractor.loadPurchaseInAppList()
            .subscribe(new Observer<List<Purchase>>() {
                @Override
                public void onSubscribe(Disposable d) { }

                @Override
                public void onNext(List<Purchase> purchases) {
                    if (CollectionUtils.isEmpty(purchases)) {
                        callback.onResult(Collections.emptyList(), 0);
                    }
                    else {
                        callback.onResult(purchases, params.requestedStartPosition);
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() { }
            });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Purchase> callback) {
        callback.onResult(Collections.emptyList());
    }
}
