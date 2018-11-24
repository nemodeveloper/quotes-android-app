package ru.nemodev.project.quotes.service.author;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.nemodev.project.quotes.entity.external.Author;

public interface AuthorService
{
    @GET("list")
    Observable<List<Author>> getAll();
}
