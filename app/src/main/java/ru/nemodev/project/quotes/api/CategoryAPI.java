package ru.nemodev.project.quotes.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.nemodev.project.quotes.api.dto.CategoryDTO;

public interface CategoryAPI
{
    @GET("list")
    Observable<List<CategoryDTO>> getAll();
}
