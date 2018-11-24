package ru.nemodev.project.quotes.service.quote.external;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import ru.nemodev.project.quotes.entity.external.Quote;

public interface QuoteService
{
    @GET("random")
    Observable<List<Quote>> getRandom(@QueryMap Map<String, String> queryParams);

    @GET("author/{authorId}")
    Observable<List<Quote>> getByAuthor(@Path("authorId") Long authorId);

    @GET("category/{categoryId}")
    Observable<List<Quote>> getByCategory(@Path("categoryId") Long categoryId);
}
