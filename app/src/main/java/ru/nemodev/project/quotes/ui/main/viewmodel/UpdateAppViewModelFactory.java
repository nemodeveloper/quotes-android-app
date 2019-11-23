package ru.nemodev.project.quotes.ui.main.viewmodel;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class UpdateAppViewModelFactory implements ViewModelProvider.Factory {

    private final Activity activity;

    public UpdateAppViewModelFactory(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (UpdateAppViewModel.class.equals(modelClass)) {
            return (T) new UpdateAppViewModel(activity);
        }

        throw new IllegalArgumentException(String.format("Не корректный класс для фабрики %s, unknown ViewModel class = %s",
                this.getClass().getName(), modelClass.getName()));
    }
}
