package ru.nemodev.project.quotes.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.nemodev.project.quotes.api.dto.AuthorDTO;

public interface AuthorAPI
{
    @GET("list")
    Observable<List<AuthorDTO>> getAll();
}
