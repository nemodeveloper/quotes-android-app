package ru.nemodev.project.quotes.ui.category.list.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import ru.nemodev.project.quotes.entity.category.Category;
import ru.nemodev.project.quotes.service.category.CategoryService;
import ru.nemodev.project.quotes.ui.category.list.source.CategoryListDataSource;


public class CategoryListViewModel extends ViewModel {

    private LiveData<PagedList<Category>> categoryList;

    public CategoryListViewModel() { }

    public LiveData<PagedList<Category>> getCategoryList(LifecycleOwner lifecycleOwner, String categoryName) {

        if (categoryList != null) {
            categoryList.removeObservers(lifecycleOwner);
        }

        DataSource.Factory<Integer, Category> factFactory = new DataSource.Factory<Integer, Category>() {
            @NonNull
            @Override
            public DataSource<Integer, Category> create() {
                return new CategoryListDataSource(categoryName, CategoryService.getInstance());
            }
        };

        categoryList = new LivePagedListBuilder<>(
                    factFactory,
                    new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setPageSize(10)
                            .setPrefetchDistance(5)
                            .build())
                    .build();

        return categoryList;
    }

}
