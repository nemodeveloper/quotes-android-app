package ru.nemodev.project.quotes.view.quote.category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.nemodev.project.quotes.view.base.BaseQuoteCardView;

public class QuoteByCategoryCardView extends BaseQuoteCardView
{
    public QuoteByCategoryCardView(@NonNull Context context)
    {
        this(context, null);
    }

    public QuoteByCategoryCardView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public QuoteByCategoryCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void showCategoryAction()
    { }
}