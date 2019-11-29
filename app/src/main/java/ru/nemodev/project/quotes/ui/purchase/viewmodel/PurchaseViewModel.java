package ru.nemodev.project.quotes.ui.purchase.viewmodel;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.android.billingclient.api.Purchase;

import ru.nemodev.project.quotes.entity.purchase.PurchaseItem;
import ru.nemodev.project.quotes.entity.purchase.PurchaseType;
import ru.nemodev.project.quotes.service.purchase.PurchaseService;


public class PurchaseViewModel extends ViewModel {

    public final MutableLiveData<Boolean> onAdsByEvent;
    public final MutableLiveData<Boolean> onWidgetByEvent;

    public final LiveData<Purchase> onPurchaseEvent;
    public final LiveData<PagedList<PurchaseItem>> purchaseList;

    private PurchaseService purchaseService;

    public PurchaseViewModel(Activity activity) {
        purchaseService = new PurchaseService(activity);

        onPurchaseEvent = purchaseService.getOnPurchaseEvent();
        purchaseList = purchaseService.getPurchaseList();

        onAdsByEvent = new MutableLiveData<>();
        onWidgetByEvent = new MutableLiveData<>();
    }

    public void buy(PurchaseItem purchaseItem) {
        purchaseService.buy(purchaseItem);
    }

    public void buy(PurchaseType purchaseType) {
        purchaseService.buy(purchaseType);
    }

    public void checkPurchase() {
        onAdsByEvent.postValue(purchaseService.isPurchase(PurchaseType.QUOTE_ADB));
        onWidgetByEvent.postValue(purchaseService.isPurchase(PurchaseType.QUOTE_WIDGET));
    }
}
