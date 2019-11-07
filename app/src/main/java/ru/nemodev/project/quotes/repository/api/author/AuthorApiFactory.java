package ru.nemodev.project.quotes.repository.api.author;

import ru.nemodev.project.quotes.core.repository.api.RetrofitApiFactory;
import ru.nemodev.project.quotes.repository.api.ServerConfig;


public class AuthorApiFactory extends RetrofitApiFactory<AuthorApi> {

    private static final String AUTHOR_ENDPOINT = ServerConfig.BASE_ENDPOINT + "author/";

    @Override
    public AuthorApi createApi() {
        return getRetrofitBuilder(AUTHOR_ENDPOINT).build().create(AuthorApi.class);
    }
}
