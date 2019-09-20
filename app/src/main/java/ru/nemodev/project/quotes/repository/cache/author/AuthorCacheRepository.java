package ru.nemodev.project.quotes.repository.cache.author;

import java.util.List;

import io.reactivex.Observable;
import ru.nemodev.project.quotes.entity.author.Author;


public interface AuthorCacheRepository
{
    Observable<List<Author>> getAll();

    void putAll(List<Author> authorList);
}
