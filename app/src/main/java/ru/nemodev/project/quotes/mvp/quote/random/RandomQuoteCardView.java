package ru.nemodev.project.quotes.mvp.quote.random;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.nemodev.project.quotes.mvp.base.BaseQuoteCardView;

public class RandomQuoteCardView extends BaseQuoteCardView
{
    public RandomQuoteCardView(@NonNull Context context)
    {
        this(context, null);
    }

    public RandomQuoteCardView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RandomQuoteCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
}