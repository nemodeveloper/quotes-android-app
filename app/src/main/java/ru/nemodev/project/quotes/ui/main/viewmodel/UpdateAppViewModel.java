package ru.nemodev.project.quotes.ui.main.viewmodel;

import android.app.Activity;
import android.content.IntentSender;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

public class UpdateAppViewModel extends ViewModel {

    public final MutableLiveData<InstallState> updateAppEvent;
    private AppUpdateManager appUpdateManager;
    private final Activity activity;

    public UpdateAppViewModel(Activity activity) {
        this.activity = activity;
        this.updateAppEvent = new MutableLiveData<>();

        appUpdateManager = AppUpdateManagerFactory.create(activity);
        appUpdateManager.getAppUpdateInfo()
                .addOnSuccessListener(appUpdateInfo -> {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo,
                                    AppUpdateType.FLEXIBLE,
                                    activity,
                                    1077);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo,
                                    AppUpdateType.IMMEDIATE,
                                    activity,
                                    1077);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(e -> { });
        appUpdateManager.registerListener(state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                updateAppEvent.postValue(state);
            }
        });

    }

    public void appUpdate() {
        appUpdateManager.completeUpdate();
    }
}
