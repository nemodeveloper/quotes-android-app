package ru.nemodev.project.quotes.service;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.nemodev.project.quotes.service.author.AuthorService;
import ru.nemodev.project.quotes.service.category.CategoryService;
import ru.nemodev.project.quotes.service.quote.external.QuoteService;

public class RetrofitServiceFactory
{
    private static final int CONNECT_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 10;

    private static final String BASE_ENDPOINT = "https://quoteformuse.ru/quotes/rest/v1/";
    private static final String QUOTE_ENDPOINT = BASE_ENDPOINT + "quote/";
    private static final String CATEGORY_ENDPOINT = BASE_ENDPOINT + "category/";
    private static final String AUTHOR_ENDPOINT = BASE_ENDPOINT + "author/";

    private static final OkHttpClient okHttpClient = createHttpClient();

    private static final QuoteService QUOTE_RETROFIT_SERVICE = buildRetrofit(QUOTE_ENDPOINT).create(QuoteService.class);
    private static final CategoryService CATEGORY_RETROFIT_SERVICE = buildRetrofit(CATEGORY_ENDPOINT).create(CategoryService.class);
    private static final AuthorService AUTHOR_RETROFIT_SERVICE = buildRetrofit(AUTHOR_ENDPOINT).create(AuthorService.class);

    @NonNull
    private static OkHttpClient createHttpClient()
    {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @NonNull
    private static Retrofit buildRetrofit(String endpoint)
    {
        return new Retrofit.Builder()
                .baseUrl(endpoint)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static QuoteService getQuoteService()
    {
        return QUOTE_RETROFIT_SERVICE;
    }

    public static CategoryService getCategoryService()
    {
        return CATEGORY_RETROFIT_SERVICE;
    }

    public static AuthorService getAuthorService()
    {
        return AUTHOR_RETROFIT_SERVICE;
    }
}
