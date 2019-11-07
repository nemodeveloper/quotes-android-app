package ru.nemodev.project.quotes.repository.api.author;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.nemodev.project.quotes.repository.api.dto.AuthorDTO;

public interface AuthorApi
{
    @GET("list")
    Observable<List<AuthorDTO>> getAll();
}
