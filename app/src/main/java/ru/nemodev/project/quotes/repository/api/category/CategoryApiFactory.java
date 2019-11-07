package ru.nemodev.project.quotes.repository.api.category;

import ru.nemodev.project.quotes.core.repository.api.RetrofitApiFactory;
import ru.nemodev.project.quotes.repository.api.ServerConfig;


public class CategoryApiFactory extends RetrofitApiFactory<CategoryApi> {

    private static final String CATEGORY_ENDPOINT = ServerConfig.BASE_ENDPOINT + "category/";

    @Override
    public CategoryApi createApi() {
        return getRetrofitBuilder(CATEGORY_ENDPOINT).build().create(CategoryApi.class);
    }
}
