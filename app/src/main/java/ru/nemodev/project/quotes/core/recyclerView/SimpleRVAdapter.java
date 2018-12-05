package ru.nemodev.project.quotes.core.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class SimpleRVAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
{
    protected final Context context;
    protected final List<T> data;

    protected SimpleRVAdapter(Context context, List<T> data)
    {
        this.context = context;
        this.data = data;
    }

    public void addItems(List<T> items)
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
