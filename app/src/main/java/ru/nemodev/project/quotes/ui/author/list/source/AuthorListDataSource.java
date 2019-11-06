package ru.nemodev.project.quotes.ui.author.list.source;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.service.author.AuthorService;

public class AuthorListDataSource extends PositionalDataSource<Author> {

    private final String authorName;
    private final AuthorService authorService;

    public AuthorListDataSource(String authorName, AuthorService authorService) {
        this.authorName = authorName;
        this.authorService = authorService;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Author> callback) {

        Observable<List<Author>> observable = StringUtils.isEmpty(authorName)
                ? authorService.getAll()
                : authorService.findByName(authorName);

        observable.subscribe(new Observer<List<Author>>() {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(List<Author> authors) {
                if (CollectionUtils.isEmpty(authors)) {
                    callback.onResult(Collections.emptyList(), 0);
                }
                else {
                    callback.onResult(authors, params.requestedStartPosition);
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
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Author> callback) {
        callback.onResult(Collections.emptyList());
    }
}
