package ru.nemodev.project.quotes.ui.purchase.source;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.Collections;
import java.util.List;

import ru.nemodev.project.quotes.entity.purchase.PurchaseItem;


public class PurchaseListDataSource extends PositionalDataSource<PurchaseItem> {

    private final List<PurchaseItem> purchaseItems;

    public PurchaseListDataSource(List<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<PurchaseItem> callback) {
        callback.onResult(purchaseItems, params.requestedStartPosition);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<PurchaseItem> callback) {
        callback.onResult(Collections.emptyList());
    }
}
