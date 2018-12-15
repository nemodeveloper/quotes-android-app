package ru.nemodev.project.quotes.entity;

import android.arch.persistence.room.Embedded;

import java.io.Serializable;

public class QuoteInfo implements Serializable
{
    @Embedded(prefix = "quote_")
    private Quote quote;

    @Embedded(prefix = "author_")
    private Author author;

    @Embedded(prefix = "category_")
    private Category category;

    public Quote getQuote()
    {
        return quote;
    }

    public void setQuote(Quote quote)
    {
        this.quote = quote;
    }

    public Author getAuthor()
    {
        return author;
    }

    public void setAuthor(Author author)
    {
        this.author = author;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }
}