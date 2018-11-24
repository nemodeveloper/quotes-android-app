package ru.nemodev.project.quotes.core.search;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerSection;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.QuoteApp;
import ru.nemodev.project.quotes.core.load.LoadDataListener;
import ru.nemodev.project.quotes.core.load.simple.SimpleLoadRVAdapter;
import ru.nemodev.project.quotes.utils.AndroidUtils;

// TODO Объеденить с AutoLoadRV - вынести в отдельный класс без наследования от IndexFastScrollRecyclerView
public class FastSearchLoadRV<T, VH extends RecyclerView.ViewHolder> extends IndexFastScrollRecyclerView
{
    private static final String TAG = "SimpleLoadRV";

    private AtomicBoolean allDataLoaded = new AtomicBoolean(false);
    private AtomicBoolean dataLoading = new AtomicBoolean(false);

    private SimpleLoadRVAdapter<T, VH> simpleLoadRVAdapter;
    private LoadDataListener<T> loadDataListener;

    public FastSearchLoadRV(Context context)
    {
        this(context, null);
    }

    public FastSearchLoadRV(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
    }

    public FastSearchLoadRV(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initialize();
    }

    protected void initialize()
    {
        setDataLoading(false);
        setAllDataLoaded(false);
    }

    public void loadData(LoadDataListener<T> loadDataListener)
    {
        setLoadDataListener(loadDataListener);
        loadNewData();
        connectToInternetEvent();
    }

    private synchronized void loadNewData()
    {
        if (!isNeedLoadNewData())
            return;

        final Observer<List<T>> loadNewItemsSubscriber = new Observer<List<T>>()
        {
            @Override
            public void onError(Throwable e)
            {
                Log.e(TAG, "loadNewData error", e);

                setDataLoading(false);
                Observable.timer(5, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>()
                        {
                            @Override
                            public void onSubscribe(Disposable d) { }

                            @Override
                            public void onNext(Long aLong)
                            {
                                loadNewData();
                            }

                            @Override
                            public void onError(Throwable e) { }

                            @Override
                            public void onComplete() { }
                        });
            }

            @Override
            public void onComplete() { }

            @Override
            public void onSubscribe(Disposable d){ }

            @Override
            public void onNext(List<T> items)
            {
                if (CollectionUtils.isEmpty(items))
                    return;

                SimpleLoadRVAdapter<T, VH> adapter = getAdapter();
                adapter.addItem(items);
                adapter.notifyItemInserted(adapter.getItemCount() - items.size());

                setAllDataLoaded(true);
                setDataLoading(false);
                getLoadDataListener().onFirstDataLoaded();

                postNewDataLoaded();
            }
        };

        setDataLoading(true);
        getLoadDataListener().getLoadObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loadNewItemsSubscriber);
    }

    protected void postNewDataLoaded()
    {
        updateIndexSearchData();
    }

    private void updateIndexSearchData()
    {
        try
        {
            Field privateField = IndexFastScrollRecyclerView.class.getDeclaredField("mScroller");
            privateField.setAccessible(true);

            IndexFastScrollRecyclerSection mScroller = (IndexFastScrollRecyclerSection) privateField.get(this);
            mScroller.onChanged();
            setIndexBarVisibility(true);
        }
        catch (NoSuchFieldException | IllegalAccessException e) { }
    }

    private void setLoadDataListener(LoadDataListener<T> loadDataListener)
    {
        this.loadDataListener = loadDataListener;
    }

    private LoadDataListener<T> getLoadDataListener()
    {
        return loadDataListener;
    }

    @Override
    public void setAdapter(Adapter adapter)
    {
        setAdapter((SimpleLoadRVAdapter<T, VH>)adapter);
    }

    public void setAdapter(SimpleLoadRVAdapter<T, VH> simpleLoadRVAdapter)
    {
        this.simpleLoadRVAdapter = simpleLoadRVAdapter;
        super.setAdapter(simpleLoadRVAdapter);
    }

    @Override
    public SimpleLoadRVAdapter<T, VH> getAdapter()
    {
        return simpleLoadRVAdapter;
    }

    private void connectToInternetEvent()
    {
        ReactiveNetwork.observeNetworkConnectivity(QuoteApp.getInstance())
                .subscribeOn(Schedulers.io())
                .filter(ConnectivityPredicate.hasState(NetworkInfo.State.CONNECTED, NetworkInfo.State.DISCONNECTED))
                .filter(ConnectivityPredicate.hasType(ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Connectivity>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Connectivity connectivity)
                    {
                        if (connectivity.state() == NetworkInfo.State.CONNECTED)
                            internetOn();
                        else
                            internetOff();
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }

    protected void internetOn()
    {
        if (isNeedLoadNewData())
            loadNewData();
    }

    protected void internetOff()
    {
        AndroidUtils.showToastMessage(R.string.connect_off_message);
    }

    private boolean isNeedLoadNewData()
    {
        return !isDataLoading() && !isAllDataLoaded();
    }

    private boolean isAllDataLoaded()
    {
        return allDataLoaded.get();
    }

    private void setAllDataLoaded(boolean allDataLoaded)
    {
        this.allDataLoaded.set(allDataLoaded);
    }

    private boolean isDataLoading()
    {
        return dataLoading.get();
    }

    private void setDataLoading(boolean dataLoading)
    {
        this.dataLoading.set(dataLoading);
    }
}
