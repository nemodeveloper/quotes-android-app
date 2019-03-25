package ru.nemodev.project.quotes.mvp.main;

import android.content.Intent;

import ru.nemodev.project.quotes.mvp.purchase.PurchaseModel;

public interface MainContract
{
    interface MainPresenter
    {
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void onDestroy();

        PurchaseModel getPurchaseModel();
    }

    interface MainView
    { }

}
