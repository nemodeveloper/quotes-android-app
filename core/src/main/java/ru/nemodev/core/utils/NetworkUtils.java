package ru.nemodev.core.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.core.app.AndroidApplication;


public final class NetworkUtils
{
    private NetworkUtils() { }

    public static Observable<Connectivity> getNetworkObservable()
    {
        return ReactiveNetwork.observeNetworkConnectivity(AndroidApplication.getInstance())
                .subscribeOn(Schedulers.io())
                .filter(ConnectivityPredicate.hasState(NetworkInfo.State.CONNECTED, NetworkInfo.State.DISCONNECTED))
                .filter(ConnectivityPredicate.hasType(ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
