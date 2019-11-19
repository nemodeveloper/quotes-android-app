package ru.nemodev.project.quotes.ui.author.list.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;
import ru.nemodev.project.quotes.service.author.AuthorService;
import ru.nemodev.project.quotes.ui.base.BaseViewModel;


public class AuthorListViewModel extends BaseViewModel {

    public final MutableLiveData<String> searchString;
    private LiveData<PagedList<Author>> authorList;

    public AuthorListViewModel() {
        super();
        this.searchString = new MutableLiveData<>();
    }

    public LiveData<PagedList<Author>> getAuthorList(LifecycleOwner lifecycleOwner, String authorName) {

        if (authorList != null) {
            authorList.removeObservers(lifecycleOwner);
        }

        DataSource.Factory<Integer, Author> factFactory = StringUtils.isEmpty(authorName)
                ? AppDataBase.getInstance().getAuthorRepository().getAllLiveData()
                : AppDataBase.getInstance().getAuthorRepository().findByNameLiveData(authorName);

        searchString.postValue(authorName);

        authorList = new LivePagedListBuilder<>(
                    factFactory,
                    new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setPageSize(500)
                            .setPrefetchDistance(100)
                            .build())
                    .build();

        return authorList;
    }

    public void onInternetEvent(boolean isAvailable) {
        if (!synced.get() && isAvailable) {
            startWorkEvent.postValue(true);
            AuthorService.getInstance().syncWithServer()
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
