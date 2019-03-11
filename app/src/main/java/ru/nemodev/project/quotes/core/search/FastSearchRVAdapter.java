package ru.nemodev.project.quotes.core.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.nemodev.project.quotes.core.recyclerView.SimpleRVAdapter;

public abstract class FastSearchRVAdapter<T, VH extends RecyclerView.ViewHolder>
        extends SimpleRVAdapter<T, VH> implements SectionIndexer, Filterable
{
    private List<Integer> sectionPositions;
    protected List<T> filteredData;

    public FastSearchRVAdapter(Context context, List<T> data)
    {
        super(context, data);
        this.filteredData = data;
        this.sectionPositions = Collections.emptyList();
    }

    protected abstract String getSearchSectionName(T item);

    @Override
    public String[] getSections()
    {
        this.sectionPositions = new ArrayList<>(filteredData.size());
        List<String> symbols = new ArrayList<>();

        for (int i = 0; i < filteredData.size(); ++i)
        {
            String symbol = getSearchSectionName(filteredData.get(i));
            if (!symbols.contains(symbol))
            {
                symbols.add(symbol);
                sectionPositions.add(i);
            }
        }

        return symbols.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int i)
    {
        return sectionPositions.get(i);
    }

    @Override
    public int getSectionForPosition(int i)
    {
        return 0;
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
