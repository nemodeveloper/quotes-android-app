package ru.nemodev.project.quotes.view.quote.author;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.nemodev.project.quotes.view.base.BaseQuoteCardView;

public class QuoteByAuthorCardView extends BaseQuoteCardView
{
    public QuoteByAuthorCardView(@NonNull Context context)
    {
        this(context, null);
    }

    public QuoteByAuthorCardView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public QuoteByAuthorCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void showAuthor() { }
}