package ru.nemodev.project.quotes.view.category.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.nemodev.project.quotes.core.search.FastSearchLoadRV;
import ru.nemodev.project.quotes.entity.external.Category;

public class CategoryLoadRV extends FastSearchLoadRV<Category, CategoryRVAdapter.CategoryViewHolder>
{
    public CategoryLoadRV(Context context)
    {
        super(context);
    }

    public CategoryLoadRV(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CategoryLoadRV(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
}
