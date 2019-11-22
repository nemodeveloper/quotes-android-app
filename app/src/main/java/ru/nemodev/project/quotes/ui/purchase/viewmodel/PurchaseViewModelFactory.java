package ru.nemodev.project.quotes.ui.purchase.viewmodel;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class PurchaseViewModelFactory implements ViewModelProvider.Factory {

    private final Activity activity;

    public PurchaseViewModelFactory(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (PurchaseViewModel.class.equals(modelClass)) {
            return (T) new PurchaseViewModel(activity);
        }

        throw new IllegalArgumentException(String.format("Не корректный класс для фабрики %s, unknown ViewModel class = %s",
                this.getClass().getName(), modelClass.getName()));
    }
}
