package ru.nemodev.project.quotes.mvp.author.list;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.service.author.AuthorService;


public class AuthorListInteractorImpl implements AuthorListContract.AuthorListInteractor
{
    @Override
    public void loadAuthors(OnFinishLoadListener onFinishLoadListener)
    {
        AuthorService.getInstance().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<Author>>()
            {
                @Override
                public void onSubscribe(Disposable d) { }

                @Override
                public void onNext(List<Author> authors)
                {
                    onFinishLoadListener.onFinishLoad(authors, false);
                }

                @Override
                public void onError(Throwable e)
                {
                    onFinishLoadListener.onLoadError(e);
                }

                @Override
                public void onComplete() { }
            });
    }
}
