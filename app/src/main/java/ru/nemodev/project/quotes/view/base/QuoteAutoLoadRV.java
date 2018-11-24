package ru.nemodev.project.quotes.view.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

import ru.nemodev.project.quotes.core.load.auto.AutoLoadRV;
import ru.nemodev.project.quotes.entity.external.Quote;

public class QuoteAutoLoadRV<VH extends RecyclerView.ViewHolder> extends AutoLoadRV<Quote, VH>
{
    public QuoteAutoLoadRV(Context context)
    {
        super(context);
    }

    public QuoteAutoLoadRV(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public QuoteAutoLoadRV(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected List<Quote> processNewData(List<Quote> data)
    {
        if (CollectionUtils.isEmpty(data))
            return data;

        Collections.shuffle(data);

        return data;
    }
}