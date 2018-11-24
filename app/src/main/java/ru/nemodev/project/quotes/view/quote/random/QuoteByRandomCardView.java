package ru.nemodev.project.quotes.view.quote.random;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.nemodev.project.quotes.view.base.BaseQuoteCardView;

public class QuoteByRandomCardView extends BaseQuoteCardView
{
    public QuoteByRandomCardView(@NonNull Context context)
    {
        this(context, null);
    }

    public QuoteByRandomCardView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public QuoteByRandomCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
}