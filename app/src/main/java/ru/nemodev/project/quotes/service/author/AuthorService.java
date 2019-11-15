package ru.nemodev.project.quotes.service.author;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.entity.author.AuthorUtils;
import ru.nemodev.project.quotes.repository.api.author.AuthorApi;
import ru.nemodev.project.quotes.repository.api.author.AuthorApiFactory;
import ru.nemodev.project.quotes.repository.db.author.AuthorRepository;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;


public class AuthorService {
    private static final AuthorService instance = new AuthorService();

    private final AuthorRepository authorRepository;
    private final AuthorApi authorApi;

    private AuthorService() {
        authorRepository = AppDataBase.getInstance().getAuthorRepository();
        authorApi = new AuthorApiFactory().createApi();
    }

    public static AuthorService getInstance() {
        return instance;
    }

    public Observable<List<Author>> getAll() {
        Observable<List<Author>> authorGatewayObservable = authorApi.getAll()
                .map(AuthorUtils::convertAuthors)
                .map(authorList -> {
                    authorRepository.add(authorList);
                    return authorList;
                })
                .onErrorResumeNext(Observable.empty());

        return Observable.concat(
                authorGatewayObservable,
                authorRepository.getAll())
                    .filter(CollectionUtils::isNotEmpty)
                    .subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> syncWithServer() {
        return authorApi.getAll()
                .map(AuthorUtils::convertAuthors)
                .map(authorList -> {
                    authorRepository.add(authorList);
                    return authorList;
                })
                .flatMap(categoryList -> Observable.just(true))
                .onErrorResumeNext(Observable.just(false))
                .subscribeOn(Schedulers.io());
    }

}