package ru.nemodev.project.quotes.entity.quote;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Calendar;

import ru.nemodev.project.quotes.repository.database.DataTypeConverter;

@Entity(tableName = "quotes",
        indices = {
            @Index("liked"),
            @Index("category_id"),
            @Index("author_id")
        })
public class Quote implements Serializable
{
    @PrimaryKey
    private Long id;

    @ColumnInfo(name = "category_id")
    @NonNull
    private Long categoryId;

    @NonNull
    private String text;

    @ColumnInfo(name = "author_id")
    @NonNull
    private Long authorId;
    private String source;
    private String year;

    @ColumnInfo(name = "liked")
    @NonNull
    private Boolean liked;

    @ColumnInfo(name = "like_date")
    @TypeConverters({DataTypeConverter.class})
    private Calendar likeDate;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Long getAuthorId()
    {
        return authorId;
    }

    public void setAuthorId(Long authorId)
    {
        this.authorId = authorId;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public Boolean getLiked()
    {
        return liked;
    }

    public void setLiked(Boolean liked)
    {
        this.liked = liked;
    }

    public Calendar getLikeDate()
    {
        return likeDate;
    }

    public void setLikeDate(Calendar likeDate)
    {
        this.likeDate = likeDate;
    }
}