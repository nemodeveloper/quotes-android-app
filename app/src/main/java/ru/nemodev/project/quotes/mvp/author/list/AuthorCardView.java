package ru.nemodev.project.quotes.mvp.author.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import org.apache.commons.lang3.StringUtils;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.Author;

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
                .buildRound("", ColorGenerator.MATERIAL.getColor(author.getId()));

        ImageView authorImage = this.findViewById(R.id.authorImg);
        if (StringUtils.isNotEmpty(author.getImageURL()))
        {
            Glide.with(getContext())
                    .load(author.getImageURL())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(drawable)
                    .error(drawable)
                    .transform(new CircleCrop())
                    .into(authorImage);
        }
        else
        {
            authorImage.setImageDrawable(drawable);
        }

        TextView authorName = this.findViewById(R.id.authorName);
        authorName.setText(author.getFullName());
    }

    public Author getAuthor()
    {
        return author;
    }
}
