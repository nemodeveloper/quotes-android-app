package ru.nemodev.project.quotes.api;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitAPIFactory
{
    private static final int CONNECT_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 10;

    private static final String BASE_ENDPOINT = "https://quoteformuse.ru/quotes/rest/v1/";
    private static final String QUOTE_ENDPOINT = BASE_ENDPOINT + "quote/";
    private static final String CATEGORY_ENDPOINT = BASE_ENDPOINT + "category/";
    private static final String AUTHOR_ENDPOINT = BASE_ENDPOINT + "author/";

    private static final OkHttpClient OK_HTTP_CLIENT = createHttpClient();

    private static final QuoteAPI QUOTE_RETROFIT_API = buildRetrofit(QUOTE_ENDPOINT).create(QuoteAPI.class);
    private static final CategoryAPI CATEGORY_RETROFIT_API = buildRetrofit(CATEGORY_ENDPOINT).create(CategoryAPI.class);
    private static final AuthorAPI AUTHOR_RETROFIT_API = buildRetrofit(AUTHOR_ENDPOINT).create(AuthorAPI.class);

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
                .client(OK_HTTP_CLIENT)
                .build();
    }

    public static QuoteAPI getQuoteAPI()
    {
        return QUOTE_RETROFIT_API;
    }

    public static CategoryAPI getCategoryAPI()
    {
        return CATEGORY_RETROFIT_API;
    }

    public static AuthorAPI getAuthorAPI()
    {
        return AUTHOR_RETROFIT_API;
    }
}
