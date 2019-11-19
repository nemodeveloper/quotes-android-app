package ru.nemodev.project.quotes.ui.category.list.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.entity.category.Category;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;
import ru.nemodev.project.quotes.service.category.CategoryService;
import ru.nemodev.project.quotes.ui.base.BaseViewModel;


public class CategoryListViewModel extends BaseViewModel {

    public final MutableLiveData<String> searchString;
    private LiveData<PagedList<Category>> categoryList;

    public CategoryListViewModel() {
        super();
        this.searchString = new MutableLiveData<>();
    }

    public LiveData<PagedList<Category>> getCategoryList(LifecycleOwner lifecycleOwner, String categoryName) {

        if (categoryList != null) {
            categoryList.removeObservers(lifecycleOwner);
        }

        DataSource.Factory<Integer, Category> factFactory = StringUtils.isEmpty(categoryName)
                ? AppDataBase.getInstance().getCategoryRepository().getAllLiveData()
                : AppDataBase.getInstance().getCategoryRepository().findByNameLiveData(categoryName);

        searchString.postValue(categoryName);

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
        if (!synced.get() && isAvailable) {
            startWorkEvent.postValue(true);
            CategoryService.getInstance().syncWithServer()
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        synced.set(aBoolean);
                        startWorkEvent.postValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        startWorkEvent.postValue(false);
                    }

                    @Override
                    public void onComplete() { }
                });
        }
    }

}
