package ru.nemodev.project.quotes.view.author.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.nemodev.project.quotes.core.search.FastSearchLoadRV;
import ru.nemodev.project.quotes.entity.external.Author;

public class AuthorLoadRV extends FastSearchLoadRV<Author, AuthorRVAdapter.AuthorViewHolder>
{
    public AuthorLoadRV(Context context)
    {
        super(context);
    }

    public AuthorLoadRV(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AuthorLoadRV(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
}
