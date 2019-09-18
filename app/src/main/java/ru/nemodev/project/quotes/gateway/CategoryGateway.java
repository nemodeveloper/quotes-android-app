package ru.nemodev.project.quotes.gateway;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.nemodev.project.quotes.gateway.dto.CategoryDTO;

public interface CategoryGateway
{
    @GET("list")
    Observable<List<CategoryDTO>> getAll();
}
