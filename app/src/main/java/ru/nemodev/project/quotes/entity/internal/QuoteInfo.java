package ru.nemodev.project.quotes.entity.internal;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.io.Serializable;
import java.util.List;

public class QuoteInfo implements Serializable
{
    @Embedded
    private QuoteInternal quoteInternal;

    @Relation(parentColumn = "author_id", entityColumn = "id")
    private List<AuthorInternal> authorInternal;

    @Relation(parentColumn = "category_id", entityColumn = "id")
    private List<CategoryInternal> categoryInternal;

    public QuoteInternal getQuoteInternal()
    {
        return quoteInternal;
    }

    public void setQuoteInternal(QuoteInternal quoteInternal)
    {
        this.quoteInternal = quoteInternal;
    }

    public List<AuthorInternal> getAuthorInternal()
    {
        return authorInternal;
    }

    public void setAuthorInternal(List<AuthorInternal> authorInternal)
    {
        this.authorInternal = authorInternal;
    }

    public List<CategoryInternal> getCategoryInternal()
    {
        return categoryInternal;
    }

    public void setCategoryInternal(List<CategoryInternal> categoryInternal)
    {
        this.categoryInternal = categoryInternal;
    }
}