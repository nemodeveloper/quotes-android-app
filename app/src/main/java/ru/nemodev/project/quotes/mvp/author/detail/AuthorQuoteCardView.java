package ru.nemodev.project.quotes.mvp.author.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.nemodev.project.quotes.mvp.base.BaseQuoteCardView;

public class AuthorQuoteCardView extends BaseQuoteCardView
{
    public AuthorQuoteCardView(@NonNull Context context)
    {
        this(context, null);
    }

    public AuthorQuoteCardView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AuthorQuoteCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void showAuthor() { }
}