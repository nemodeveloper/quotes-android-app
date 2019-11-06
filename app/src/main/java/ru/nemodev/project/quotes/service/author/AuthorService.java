package ru.nemodev.project.quotes.service.author;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.entity.author.AuthorUtils;
import ru.nemodev.project.quotes.repository.cache.author.AuthorCacheRepository;
import ru.nemodev.project.quotes.repository.cache.author.AuthorCacheRepositoryImpl;
import ru.nemodev.project.quotes.repository.db.author.AuthorRepository;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;
import ru.nemodev.project.quotes.repository.gateway.RetrofitFactory;


public class AuthorService {
    private static final AuthorService instance = new AuthorService();

    private final AuthorCacheRepository authorCacheRepository;
    private final AuthorRepository authorRepository;

    private AuthorService() {
        authorCacheRepository = new AuthorCacheRepositoryImpl();
        authorRepository = AppDataBase.getInstance().getAuthorRepository();
    }

    public static AuthorService getInstance() {
        return instance;
    }

    public Observable<List<Author>> getAll() {
        Observable<List<Author>> authorGatewayObservable = RetrofitFactory.getAuthorAPI().getAll()
                .map(AuthorUtils::convertAuthors)
                .map(authorList -> {
                    authorRepository.add(authorList);
                    authorCacheRepository.putAll(authorList);
                    return authorList;
                })
                .onErrorResumeNext(Observable.empty());

        return Observable.concat(
                authorCacheRepository.getAll(),
                authorGatewayObservable,
                authorRepository.getAll()
                        .map(authorList -> {
                            authorCacheRepository.putAll(authorList);
                            return authorList;
                        }))
                    .filter(CollectionUtils::isNotEmpty)
                    .first(Collections.emptyList())
                    .toObservable()
                    .subscribeOn(Schedulers.io());
    }

    public Observable<List<Author>> findByName(String name) {
        return getAll()
                .flatMap(authors -> {
                    List<Author> filteredAuthors = new ArrayList<>();
                    for (Author author : authors) {
                        if (author.getFullName().toLowerCase().contains(name.toLowerCase())) {
                            filteredAuthors.add(author);
                        }
                    }
                    return Observable.just(filteredAuthors);
                });
    }

}