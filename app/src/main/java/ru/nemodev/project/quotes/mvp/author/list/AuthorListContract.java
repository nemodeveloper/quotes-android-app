package ru.nemodev.project.quotes.mvp.author.list;

import java.util.List;

import ru.nemodev.project.quotes.entity.author.Author;

public interface AuthorListContract
{
    interface AuthorListView
    {
        void showLoader();
        void hideLoader();
        void showAuthors(List<Author> authors);
    }

    interface AuthorListPresenter
    {
        void loadAuthors();
    }

    interface AuthorListInteractor
    {
        interface OnFinishLoadListener
        {
            void onFinishLoad(List<Author> authors, boolean fromCache);
            void onLoadError(Throwable t);
        }

        void loadAuthors(OnFinishLoadListener onFinishLoadListener, boolean fromCache);
    }
}
