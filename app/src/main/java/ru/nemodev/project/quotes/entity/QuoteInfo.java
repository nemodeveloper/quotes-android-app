package ru.nemodev.project.quotes.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.io.Serializable;
import java.util.List;

public class QuoteInfo implements Serializable
{
    @Embedded
    private Quote quote;

    @Relation(parentColumn = "author_id", entityColumn = "id", entity = Author.class)
    private List<Author> authors;

    @Relation(parentColumn = "category_id", entityColumn = "id", entity = Category.class)
    private List<Category> categories;

    @Ignore
    private Author author;

    @Ignore
    private Category category;

    public Quote getQuote()
    {
        return quote;
    }

    public void setQuote(Quote quote)
    {
        this.quote = quote;
    }

    public List<Author> getAuthors()
    {
        return authors;
    }

    public void setAuthors(List<Author> authors)
    {
        this.authors = authors;
        this.author = authors.get(0);
    }

    public List<Category> getCategories()
    {
        return categories;
    }

    public void setCategories(List<Category> categories)
    {
        this.categories = categories;
        this.category = categories.get(0);
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