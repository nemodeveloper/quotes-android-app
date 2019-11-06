package ru.nemodev.project.quotes.ui.category.list.source;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.entity.category.Category;
import ru.nemodev.project.quotes.service.category.CategoryService;

public class CategoryListDataSource extends PositionalDataSource<Category> {

    private final String categoryName;
    private final CategoryService categoryService;

    public CategoryListDataSource(String categoryName, CategoryService categoryService) {
        this.categoryName = categoryName;
        this.categoryService = categoryService;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Category> callback) {

        Observable<List<Category>> observable = StringUtils.isEmpty(categoryName)
                ? categoryService.getAll()
                : categoryService.findByName(categoryName);

        observable.subscribe(new Observer<List<Category>>() {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(List<Category> categories) {
                if (CollectionUtils.isEmpty(categories)) {
                    callback.onResult(Collections.emptyList(), 0);
                }
                else {
                    callback.onResult(categories, params.requestedStartPosition);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() { }
        });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Category> callback) {
        callback.onResult(Collections.emptyList());
    }
}
