package ru.nemodev.project.quotes.repository.gateway.author;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.nemodev.project.quotes.repository.gateway.dto.AuthorDTO;

public interface AuthorGateway
{
    @GET("list")
    Observable<List<AuthorDTO>> getAll();
}
