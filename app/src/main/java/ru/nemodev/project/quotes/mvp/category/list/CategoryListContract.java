package ru.nemodev.project.quotes.mvp.category.list;

import java.util.List;

import ru.nemodev.project.quotes.entity.Category;

public interface CategoryListContract
{
    interface CategoryListView
    {
        void showLoader();
        void hideLoader();
        void showCategories(List<Category> categories);
    }

    interface CategoryListPresenter
    {
        void loadCategory();
    }

    interface CategoryListIntractor
    {
        interface OnFinishLoadListener
        {
            void onFinishLoad(List<Category> categories);
            void onLoadError(Throwable t);
        }

        void loadCategories(OnFinishLoadListener onFinishLoadListener);
    }
}
