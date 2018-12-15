package ru.nemodev.project.quotes.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "quotes")
public class Quote implements Serializable
{
    @PrimaryKey
    private Long id;
    @ColumnInfo(name = "category_id")
    private Long categoryId;
    private String text;
    @ColumnInfo(name = "author_id")
    private Long authorId;
    private String source;
    private String year;

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
}