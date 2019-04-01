package ru.nemodev.project.quotes.mvp.main;

import android.content.Intent;

import ru.nemodev.project.quotes.mvp.purchase.BillingEventListener;
import ru.nemodev.project.quotes.mvp.purchase.PurchaseModel;

public interface MainContract
{
    interface MainPresenter
    {
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void onDestroy();

        PurchaseModel getPurchaseModel();
        void setBillingEventListener(BillingEventListener billingEventListener);

        void checkAppUpdate();
    }

    interface MainView
    {
        void showUpdateDialog();
        void showMainContent();
        void showDisableAdbDialog();
    }

}
