package ru.nemodev.project.quotes.mvp.base;

import ru.nemodev.project.quotes.entity.Author;
import ru.nemodev.project.quotes.entity.Category;

public interface OnQuoteCardClickListener
{
    void onAuthorClick(Author author);
    void onCategoryClick(Category category);

}
