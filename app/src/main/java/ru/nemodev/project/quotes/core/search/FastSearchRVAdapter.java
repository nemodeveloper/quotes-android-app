package ru.nemodev.project.quotes.core.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.SectionIndexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.nemodev.project.quotes.core.recyclerView.SimpleRVAdapter;

public abstract class FastSearchRVAdapter<T, VH extends RecyclerView.ViewHolder>
        extends SimpleRVAdapter<T, VH> implements SectionIndexer
{
    private List<Integer> sectionPositions;

    public FastSearchRVAdapter(Context context, List<T> data)
    {
        super(context, data);
        this.sectionPositions = Collections.emptyList();
    }

    protected abstract String getSearchSectionName(T item);

    @Override
    public String[] getSections()
    {
        this.sectionPositions = new ArrayList<>(data.size());
        List<String> symbols = new ArrayList<>();

        for (int i = 0; i < data.size(); ++i)
        {
            String symbol = getSearchSectionName(data.get(i));
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
}
