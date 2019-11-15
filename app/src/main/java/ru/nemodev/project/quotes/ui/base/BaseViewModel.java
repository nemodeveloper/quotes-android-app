package ru.nemodev.project.quotes.ui.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class BaseViewModel extends ViewModel {

    public final MutableLiveData<Boolean> startWorkEvent;
    protected final AtomicBoolean synced;

    public BaseViewModel() {
        this.synced = new AtomicBoolean(false);
        this.startWorkEvent = new MutableLiveData<>();
    }

}
