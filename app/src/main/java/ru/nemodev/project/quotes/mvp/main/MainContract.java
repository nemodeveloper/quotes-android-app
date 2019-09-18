package ru.nemodev.project.quotes.mvp.main;

import android.content.Intent;

import ru.nemodev.project.quotes.mvp.purchase.BillingEventListener;
import ru.nemodev.project.quotes.mvp.purchase.PurchaseInteractor;

public interface MainContract
{
    interface MainPresenter
    {
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void onDestroy();

        PurchaseInteractor getPurchaseInteractor();
        void setBillingEventListener(BillingEventListener billingEventListener);

        void checkAppUpdate();
    }

    interface MainView
    {
        void showUpdateDialog();
        void showMainContent();
        void showDisableAdsDialog();
    }

}
