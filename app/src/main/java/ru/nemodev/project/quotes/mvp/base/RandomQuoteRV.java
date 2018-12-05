package ru.nemodev.project.quotes.mvp.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import ru.nemodev.project.quotes.core.recyclerView.BaseLoadingRV;
import ru.nemodev.project.quotes.entity.external.Quote;

public class RandomQuoteRV<VH extends RecyclerView.ViewHolder> extends BaseLoadingRV<Quote, VH>
{
    public RandomQuoteRV(Context context)
    {
        super(context);
    }

    public RandomQuoteRV(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RandomQuoteRV(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
}