package ru.nemodev.project.quotes.ui.base;

import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.entity.category.Category;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;

public interface OnQuoteCardClickListener
{
    void onAuthorClick(Author author);
    void onCategoryClick(Category category);
    void onLikeClick(QuoteInfo quoteInfo);
    void onShareClick(QuoteInfo quoteInfo);
    void onWidgetClick(QuoteInfo quoteInfo);
}
