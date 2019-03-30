package ru.nemodev.project.quotes.mvp.category.detail;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.nemodev.project.quotes.mvp.base.BaseQuoteCardView;

public class CategoryQuoteCardView extends BaseQuoteCardView
{
    public CategoryQuoteCardView(@NonNull Context context)
    {
        this(context, null);
    }

    public CategoryQuoteCardView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CategoryQuoteCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initCategoryAction()
    { }
}