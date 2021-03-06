package ru.nemodev.project.quotes.service.category;


import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.category.Category;
import ru.nemodev.project.quotes.entity.category.CategoryUtils;
import ru.nemodev.project.quotes.repository.api.category.CategoryApi;
import ru.nemodev.project.quotes.repository.api.category.CategoryApiFactory;
import ru.nemodev.project.quotes.repository.db.category.CategoryRepository;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;


public class CategoryService
{
    private static final CategoryService instance = new CategoryService();

    private final CategoryRepository categoryRepository;
    private final CategoryApi categoryApi;

    private CategoryService() {
        categoryRepository = AppDataBase.getInstance().getCategoryRepository();
        categoryApi = new CategoryApiFactory().createApi();
    }

    public static CategoryService getInstance()
    {
        return instance;
    }

    public Observable<List<Category>> getAll()
    {
        Observable<List<Category>> categoryGatewayObservable = categoryApi.getAll()
                .map(CategoryUtils::convertCategories)
                .map(categoryList -> {
                    categoryRepository.add(categoryList);
                    return categoryList;
                })
                .onErrorResumeNext(Observable.empty());

        return Observable.concat(
                categoryGatewayObservable,
                categoryRepository.getAll())
                    .filter(CollectionUtils::isNotEmpty)
                    .subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> syncWithServer() {
        return categoryApi.getAll()
                .map(CategoryUtils::convertCategories)
                .map(categoryList -> {
                    categoryRepository.add(categoryList);
                    return categoryList;
                })
                .flatMap(categoryList -> Observable.just(true))
                .onErrorResumeNext(Observable.just(false))
                .subscribeOn(Schedulers.io());
    }
}