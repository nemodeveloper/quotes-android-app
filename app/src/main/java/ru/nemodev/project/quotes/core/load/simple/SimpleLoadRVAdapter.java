package ru.nemodev.project.quotes.core.load.simple;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleLoadRVAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
{
    protected final Context context;
    protected final List<T> data;

    protected SimpleLoadRVAdapter(Context context, int initialSize)
    {
        this.context = context;
        this.data = new ArrayList<>(initialSize);
    }

    public void addItem(List<T> items)
    {
        data.addAll(items);
    }

    public T getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }
}
