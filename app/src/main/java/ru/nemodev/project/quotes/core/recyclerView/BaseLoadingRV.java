package ru.nemodev.project.quotes.core.recyclerView;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


public class BaseLoadingRV<T, VH extends RecyclerView.ViewHolder> extends RecyclerView
{
    protected int batchSize;
    protected SimpleRVAdapter<T, VH> simpleRVAdapter;

    protected volatile AtomicBoolean dataLoading = new AtomicBoolean(false);

    public BaseLoadingRV(Context context)
    {
        this(context, null);
    }

    public BaseLoadingRV(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
    }

    public BaseLoadingRV(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize()
    {
        setBatchSize(200);
        setDataLoading(false);
    }

    public boolean isNeedLoadNewData()
    {
        return !isDataLoading()
                && getLastVisibleItemPosition() >= getAdapter().getItemCount() - (getBatchSize() / 2);
    }

    public int getLastVisibleItemPosition()
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
        throw new RuntimeException("Unknown LayoutManager class: " + recyclerViewLMClass.toString());
    }


    public int getBatchSize()
    {
        return batchSize;
    }

    public void setBatchSize(int batchSize)
    {
        this.batchSize = batchSize;
    }

    public boolean isDataLoading()
    {
        return dataLoading.get();
    }

    public void setDataLoading(boolean dataLoading)
    {
        this.dataLoading.set(dataLoading);
    }

    @Override
    public void setAdapter(Adapter adapter)
    {
        setAdapter((SimpleRVAdapter<T, VH>)adapter);
    }

    public void setAdapter(SimpleRVAdapter<T, VH> autoLoadingRecyclerViewAdapter)
    {
        this.simpleRVAdapter = autoLoadingRecyclerViewAdapter;
        super.setAdapter(autoLoadingRecyclerViewAdapter);
    }

    @Override
    public SimpleRVAdapter<T, VH> getAdapter()
    {
        return simpleRVAdapter;
    }

}