package ru.nemodev.project.quotes.core.load.auto;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.QuoteApp;
import ru.nemodev.project.quotes.core.load.LoadDataListener;
import ru.nemodev.project.quotes.core.load.LoadEventInfo;
import ru.nemodev.project.quotes.core.load.simple.SimpleLoadRVAdapter;
import ru.nemodev.project.quotes.exception.AutoLoadRVException;
import ru.nemodev.project.quotes.utils.AndroidUtils;

public class AutoLoadRV<T, VH extends RecyclerView.ViewHolder> extends RecyclerView
{
    private static final String TAG = "AutoLoadRV";

    protected PublishSubject<LoadEventInfo> scrollLoadingChannel;
    protected int batchSize;
    protected LoadDataListener<T> loadDataListener;
    protected SimpleLoadRVAdapter<T, VH> simpleLoadRVAdapter;

    protected volatile AtomicBoolean allDataLoaded = new AtomicBoolean(false);
    protected volatile AtomicBoolean dataLoading = new AtomicBoolean(false);
    protected volatile AtomicBoolean firstDataLoaded = new AtomicBoolean(false);

    public AutoLoadRV(Context context)
    {
        this(context, null);
    }

    public AutoLoadRV(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
    }

    public AutoLoadRV(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initialize();
    }

    public void loadData(LoadDataListener<T> loadDataListener)
    {
        setLoadDataListener(loadDataListener);

        if (isAllDataLoaded())
            return;

        if (getLoadDataListener().isInfinitySource())
        {
            startScrollingChannel();
            subscribeToLoadingChannel();
        }
        loadNewData(new LoadEventInfo(getAdapter().getItemCount(), getBatchSize()));

        connectToInternetEvent();
    }

    private void initialize()
    {
        this.scrollLoadingChannel = PublishSubject.create();

        setBatchSize(50);
        setDataLoading(false);
        setAllDataLoaded(false);
        setFirstDataLoaded(false);
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
            loadNewData(new LoadEventInfo(getAdapter().getItemCount(), getBatchSize()));
    }

    protected void internetOff()
    {
        AndroidUtils.showToastMessage(R.string.connect_off_message);
    }

    private void startScrollingChannel()
    {
        addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (isNeedLoadNewData())
                {
                    scrollLoadingChannel.onNext(new LoadEventInfo(getAdapter().getItemCount(), getBatchSize()));
                }
            }
        });
    }

    private boolean isNeedLoadNewData()
    {
        return !isDataLoading()
                && !isAllDataLoaded()
                && getLastVisibleItemPosition() >= getAdapter().getItemCount() - (getBatchSize() / 2);
    }

    private void subscribeToLoadingChannel()
    {
        scrollLoadingChannel.subscribe(new Observer<LoadEventInfo>()
        {
            @Override
            public void onError(Throwable e)
            {
                Log.e(TAG, "subscribeToLoadingChannel error", e);
            }

            @Override
            public void onComplete() { }

            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(LoadEventInfo loadEventInfo)
            {
                loadNewData(loadEventInfo);
            }
        });
    }

    private synchronized void loadNewData(LoadEventInfo loadEventInfo)
    {
        if (!isNeedLoadNewData())
            return;

        Observer<List<T>> loadNewItemsSubscriber = new Observer<List<T>>()
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
                                loadNewData(loadEventInfo);
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
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(List<T> items)
            {
                if (CollectionUtils.isNotEmpty(items))
                {
                    items = processNewData(items);

                    SimpleLoadRVAdapter<T, VH> adapter = getAdapter();
                    adapter.addItem(items);
                    adapter.notifyItemInserted(adapter.getItemCount() - items.size());

                    if (getLoadDataListener().isInfinitySource())
                        subscribeToLoadingChannel();
                    else
                        setAllDataLoaded(true);
                }
                else
                {
                    setAllDataLoaded(true);
                }
                setDataLoading(false);

                if (!isFirstDataLoaded())
                {
                    getLoadDataListener().onFirstDataLoaded();
                    setFirstDataLoaded(true);
                }
            }
        };

        setDataLoading(true);
        getLoadDataListener().getLoadObservable(loadEventInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loadNewItemsSubscriber);
    }

    private int getLastVisibleItemPosition()
    {
        Class recyclerViewLMClass = getLayoutManager().getClass();
        if (recyclerViewLMClass == LinearLayoutManager.class || LinearLayoutManager.class.isAssignableFrom(recyclerViewLMClass))
        {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager)getLayoutManager();
            return linearLayoutManager.findLastVisibleItemPosition();
        }
        else if (recyclerViewLMClass == StaggeredGridLayoutManager.class || StaggeredGridLayoutManager.class.isAssignableFrom(recyclerViewLMClass))
        {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager)getLayoutManager();
            int[] into = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
            List<Integer> intoList = new ArrayList<>();
            for (int i : into)
            {
                intoList.add(i);
            }
            return Collections.max(intoList);
        }
        throw new AutoLoadRVException("Unknown LayoutManager class: " + recyclerViewLMClass.toString());
    }

    protected List<T> processNewData(List<T> data)
    {
        return data;
    }

    public int getBatchSize()
    {
        return batchSize;
    }

    public void setBatchSize(int batchSize)
    {
        this.batchSize = batchSize;
    }

    private void setLoadDataListener(LoadDataListener<T> loadDataListener)
    {
        this.loadDataListener = loadDataListener;
    }

    private LoadDataListener<T> getLoadDataListener()
    {
        return loadDataListener;
    }

    private boolean isAllDataLoaded()
    {
        return allDataLoaded.get();
    }

    private void setAllDataLoaded(boolean allDataLoaded)
    {
        this.allDataLoaded.set(allDataLoaded);
        if (isAllDataLoaded())
        {
            clearOnScrollListeners();
        }
    }

    public boolean isDataLoading()
    {
        return dataLoading.get();
    }

    public void setDataLoading(boolean dataLoading)
    {
        this.dataLoading.set(dataLoading);
    }

    public boolean isFirstDataLoaded()
    {
        return firstDataLoaded.get();
    }

    public void setFirstDataLoaded(boolean firstDataLoaded)
    {
        this.firstDataLoaded.set(firstDataLoaded);
    }

    @Override
    public void setAdapter(Adapter adapter)
    {
        setAdapter((SimpleLoadRVAdapter<T, VH>)adapter);
    }

    public void setAdapter(SimpleLoadRVAdapter<T, VH> autoLoadingRecyclerViewAdapter)
    {
        this.simpleLoadRVAdapter = autoLoadingRecyclerViewAdapter;
        super.setAdapter(autoLoadingRecyclerViewAdapter);
    }

    @Override
    public SimpleLoadRVAdapter<T, VH> getAdapter()
    {
        return simpleLoadRVAdapter;
    }

//    @Override
//    protected void onDetachedFromWindow()
//    {
//        setAdapter(null);
//        scrollLoadingChannel.onComplete();
//        super.onDetachedFromWindow();
//    }
//
//    @Override
//    public Parcelable onSaveInstanceState()
//    {
//        Parcelable superState = super.onSaveInstanceState();
//        SavedState ss = new SavedState(superState);
//        ss.firstDataLoadedSaved = isFirstDataLoaded();
//        ss.allDataLoadedSaved = isAllDataLoaded();
//        return ss;
//    }
//
//    @Override
//    public void onRestoreInstanceState(Parcelable state)
//    {
//        super.onRestoreInstanceState(state);
//        if (state.getClass() == SavedState.class)
//        {
//            SavedState ss = (SavedState) state;
//            super.onRestoreInstanceState(ss.getSuperState());
//
//            onFirstDataLoaded = ss.firstDataLoadedSaved;
//            allDataLoaded = ss.allDataLoadedSaved;
//        }
//    }
//
//    public static class SavedState extends RecyclerView.BaseSavedState
//    {
//        boolean firstDataLoadedSaved;
//        boolean allDataLoadedSaved;
//
//        SavedState(Parcelable superState) {
//            super(superState);
//        }
//
//        @Override
//        public void writeToParcel(Parcel out, int flags)
//        {
//            super.writeToParcel(out, flags);
//            boolean params[] = new boolean[] {firstDataLoadedSaved, allDataLoadedSaved};
//            out.writeBooleanArray(params);
//        }
//
//        public static final Creator<SavedState> CREATOR = new Creator<SavedState>()
//        {
//            public SavedState createFromParcel(Parcel in) {
//                return new SavedState(in);
//            }
//
//            public SavedState[] newArray(int size) {
//                return new SavedState[size];
//            }
//        };
//
//        private SavedState(Parcel in)
//        {
//            super(in);
//            boolean params[] = new boolean[2];
//            in.readBooleanArray(params);
//            firstDataLoadedSaved = params[0];
//            allDataLoadedSaved = params[1];
//        }
//    }
}
