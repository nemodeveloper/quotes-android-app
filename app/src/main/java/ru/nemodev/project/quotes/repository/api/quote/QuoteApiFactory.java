package ru.nemodev.project.quotes.repository.api.quote;

import ru.nemodev.project.quotes.core.repository.api.RetrofitApiFactory;
import ru.nemodev.project.quotes.repository.api.ServerConfig;


public class QuoteApiFactory extends RetrofitApiFactory<QuoteApi> {

    private static final String QUOTE_ENDPOINT = ServerConfig.BASE_ENDPOINT + "quote/";

    @Override
    public QuoteApi createApi() {
        return getRetrofitBuilder(QUOTE_ENDPOINT).build().create(QuoteApi.class);
    }
}
