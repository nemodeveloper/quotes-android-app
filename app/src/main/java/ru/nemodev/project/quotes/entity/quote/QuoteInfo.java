package ru.nemodev.project.quotes.entity.quote;


import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;

import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.entity.category.Category;

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
    }

    public List<Category> getCategories()
    {
        return categories;
    }

    public void setCategories(List<Category> categories)
    {
        this.categories = categories;
    }

    public Author getAuthor()
    {
        if (author == null && CollectionUtils.isNotEmpty(authors))
            author = authors.get(0);

        return author;
    }

    public void setAuthor(Author author)
    {
        this.author = author;
    }

    public Category getCategory()
    {
        if (category == null && CollectionUtils.isNotEmpty(categories))
            category = categories.get(0);

        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof QuoteInfo)) {
            return false;
        }
        Quote what = ((QuoteInfo) obj).getQuote();
        return quote.getId().equals(what.getId())
                && quote.getText().equals(what.getText())
                && quote.getLiked().equals(what.getLiked());
    }
}