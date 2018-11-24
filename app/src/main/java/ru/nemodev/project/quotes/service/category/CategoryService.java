package ru.nemodev.project.quotes.service.category;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.nemodev.project.quotes.entity.external.Category;

public interface CategoryService
{
    @GET("list")
    Observable<List<Category>> getAll();
}
