package ru.nemodev.project.quotes.repository.cache.category;

import java.util.List;

import io.reactivex.Observable;
import ru.nemodev.project.quotes.entity.category.Category;

public interface CategoryCacheRepository {

    Observable<List<Category>> getAll();

    void putAll(List<Category> categoryList);
}
