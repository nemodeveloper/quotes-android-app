package ru.nemodev.project.quotes.view.author.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.external.Author;

public class AuthorCardView extends CardView
{
    private Author author;

    public AuthorCardView(@NonNull Context context)
    {
        this(context, null);
    }

    public AuthorCardView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AuthorCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setAuthor(Author author)
    {
        this.author = author;

        TextDrawable drawable = TextDrawable.builder()
                .buildRound("", ColorGenerator.MATERIAL.getRandomColor());

        ImageView authorImage = this.findViewById(R.id.authorImg);
        authorImage.setImageDrawable(drawable);

        TextView authorName = this.findViewById(R.id.authorName);
        authorName.setText(author.getFullName());
    }

    public Author getAuthor()
    {
        return author;
    }
}
