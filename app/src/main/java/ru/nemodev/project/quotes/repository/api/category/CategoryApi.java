package ru.nemodev.project.quotes.repository.api.category;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.nemodev.project.quotes.repository.api.dto.CategoryDTO;

public interface CategoryApi
{
    @GET("list")
    Observable<List<CategoryDTO>> getAll();
}
