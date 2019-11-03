package ru.nemodev.project.quotes.core.recyclerView;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;


public abstract class FastSearchRVAdapter<T, VH extends RecyclerView.ViewHolder>
        extends SimpleRVAdapter<T, VH> implements Filterable
{
    protected List<T> filteredData;

    public FastSearchRVAdapter(Context context, List<T> data)
    {
        super(context, data);
        this.filteredData = data;
    }

    @Override
    public T getItem(int position)
    {
        return filteredData.get(position);
    }

    @Override
    public int getItemCount()
    {
        return filteredData.size();
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                if (StringUtils.isEmpty(constraint))
                {
                    filteredData = data;
                }
                else
                {
                    filteredData = getFilteredData(constraint.toString());
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredData;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                notifyDataSetChanged();
            }
        };
    }

    protected abstract List<T> getFilteredData(String rawSearch);
}
