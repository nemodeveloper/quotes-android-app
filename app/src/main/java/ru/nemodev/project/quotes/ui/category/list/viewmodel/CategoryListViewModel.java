package ru.nemodev.project.quotes.ui.category.list.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.entity.category.Category;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;
import ru.nemodev.project.quotes.service.category.CategoryService;


public class CategoryListViewModel extends ViewModel {

    private LiveData<PagedList<Category>> categoryList;
    private Boolean synced;

    public CategoryListViewModel() {
        this.synced = false;
    }

    public LiveData<PagedList<Category>> getCategoryList(LifecycleOwner lifecycleOwner, String categoryName) {

        if (categoryList != null) {
            categoryList.removeObservers(lifecycleOwner);
        }

        DataSource.Factory<Integer, Category> factFactory = StringUtils.isEmpty(categoryName)
                ? AppDataBase.getInstance().getCategoryRepository().getAllLiveData()
                : AppDataBase.getInstance().getCategoryRepository().findByNameLiveData(categoryName);

        categoryList = new LivePagedListBuilder<>(
                factFactory,
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(500)
                        .setPrefetchDistance(100)
                        .build())
                .build();

        return categoryList;
    }

    public void onInternetEvent(boolean isAvailable) {
        if (!synced && isAvailable) {
            CategoryService.getInstance().syncWithServer()
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        synced = aBoolean;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        }
    }

}
