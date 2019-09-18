package ru.nemodev.project.quotes.mvp.quote.random;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import ru.nemodev.project.quotes.core.recyclerView.BaseLoadingRV;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;

public class RandomQuoteRV<VH extends RecyclerView.ViewHolder> extends BaseLoadingRV<QuoteInfo, VH>
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