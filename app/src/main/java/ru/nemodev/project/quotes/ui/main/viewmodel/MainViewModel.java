package ru.nemodev.project.quotes.ui.main.viewmodel;

import android.app.Activity;
import android.net.NetworkInfo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.billingclient.api.Purchase;

import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.app.AndroidApplication;
import ru.nemodev.project.quotes.entity.purchase.PurchaseType;
import ru.nemodev.project.quotes.utils.NetworkUtils;
import ru.nemodev.project.quotes.widget.WidgetUtils;


public class MainViewModel extends ViewModel {

    public final MutableLiveData<NetworkInfo.State> networkState;
    private final Disposable internetEventsDisposable;

    private Activity activity;

    public MainViewModel(Activity activity) {
        this.activity = activity;
        networkState = new MutableLiveData<>();
        internetEventsDisposable = NetworkUtils.getNetworkObservable()
                .subscribe(connectivity -> networkState.postValue(connectivity.state()));
    }

    public void onPurchase(Purchase purchase) {
        if (PurchaseType.QUOTE_WIDGET.getSku().equals(purchase.getSku())) {
            AndroidApplication.getInstance().getAppSetting().setBoolean(WidgetUtils.IS_PURCHASE_QUOTE_WIDGET_KEY, true);
        }
    }

    public void onWidgetBuy(Boolean isBuy) {
        AndroidApplication.getInstance().getAppSetting().setBoolean(WidgetUtils.IS_PURCHASE_QUOTE_WIDGET_KEY, isBuy);
    }

    @Override
    protected void onCleared() {
        internetEventsDisposable.dispose();
        super.onCleared();
    }
}
