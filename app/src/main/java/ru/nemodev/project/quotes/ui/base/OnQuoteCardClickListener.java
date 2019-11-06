package ru.nemodev.project.quotes.ui.base;

import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.entity.category.Category;

public interface OnQuoteCardClickListener
{
    void onAuthorClick(Author author);
    void onCategoryClick(Category category);

}
