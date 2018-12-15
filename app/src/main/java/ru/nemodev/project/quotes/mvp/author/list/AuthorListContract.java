package ru.nemodev.project.quotes.mvp.author.list;

import java.util.List;

import ru.nemodev.project.quotes.entity.Author;

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

    interface AuthorListIntractor
    {
        interface OnFinishLoadListener
        {
            void onFinishLoad(List<Author> authors);
            void onLoadError(Throwable t);
        }

        void loadAuthors(OnFinishLoadListener onFinishLoadListener);
    }
}
