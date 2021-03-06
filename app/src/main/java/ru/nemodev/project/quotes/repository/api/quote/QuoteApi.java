package ru.nemodev.project.quotes.repository.api.quote;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import ru.nemodev.project.quotes.repository.api.dto.QuoteDTO;

public interface QuoteApi
{
    @GET("random")
    Observable<List<QuoteDTO>> getRandom(@QueryMap Map<String, String> queryParams);

    @GET("author/{authorId}")
    Observable<List<QuoteDTO>> getByAuthor(@Path("authorId") Long authorId);

    @GET("category/{categoryId}")
    Observable<List<QuoteDTO>> getByCategory(@Path("categoryId") Long categoryId);
}
