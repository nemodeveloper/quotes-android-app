package ru.nemodev.project.quotes.ui.author.list.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.service.author.AuthorService;
import ru.nemodev.project.quotes.ui.author.list.source.AuthorListDataSource;

public class AuthorListViewModel extends ViewModel {

    private LiveData<PagedList<Author>> authorList;

    public AuthorListViewModel() { }

    public LiveData<PagedList<Author>> getAuthorList(LifecycleOwner lifecycleOwner, String authorName) {

        if (authorList != null) {
            authorList.removeObservers(lifecycleOwner);
        }

        DataSource.Factory<Integer, Author> factFactory = new DataSource.Factory<Integer, Author>() {
            @NonNull
            @Override
            public DataSource<Integer, Author> create() {
                return new AuthorListDataSource(authorName, AuthorService.getInstance());
            }
        };

        authorList = new LivePagedListBuilder<>(
                    factFactory,
                    new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setPageSize(10)
                            .setPrefetchDistance(5)
                            .build())
                    .build();

        return authorList;
    }

}
