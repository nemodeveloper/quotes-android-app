package ru.nemodev.project.quotes.ui.author.detail;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.nemodev.project.quotes.ui.base.BaseQuoteCardView;

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