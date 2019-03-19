package ru.nemodev.project.quotes.mvp.category.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.recyclerView.FastSearchRVAdapter;
import ru.nemodev.project.quotes.core.recyclerView.OnItemClickRVListener;
import ru.nemodev.project.quotes.entity.Category;

public class CategoryListAdapter extends FastSearchRVAdapter<Category, CategoryListAdapter.CategoryViewHolder>
{
    private final OnItemClickRVListener<Category> onItemClickRVListener;

    public CategoryListAdapter(Context context, List<Category> data, OnItemClickRVListener<Category> onItemClickRVListener)
    {
        super(context, data);
        this.onItemClickRVListener = onItemClickRVListener;
    }

    @Override
    protected String getSearchSectionName(Category item)
    {
        return item.getName().substring(0, 1).toUpperCase();
    }

    @Override
    protected List<Category> getFilteredData(String rawSearch)
    {
        String searchLowerCase = rawSearch.toLowerCase();

        List<Category> filteredCategories = new ArrayList<>();
        for (Category category : data)
        {
            if (category.getName().toLowerCase().contains(searchLowerCase))
            {
                filteredCategories.add(category);
            }
        }

        return filteredCategories;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        private final CategoryCardView categoryCardView;

        public CategoryViewHolder(CategoryCardView categoryCardView)
        {
            super(categoryCardView);
            this.categoryCardView = categoryCardView;
        }

        public void setCategory(final Category category)
        {
            categoryCardView.setCategory(category);
        }

        public Category getCategory()
        {
            return categoryCardView.getCategory();
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new CategoryViewHolder((CategoryCardView) LayoutInflater.from(context).inflate(R.layout.category_card_item, parent, false));
    }

    @Override
    protected void doOnBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int position)
    {
        categoryViewHolder.setCategory(getItem(position));
        categoryViewHolder.categoryCardView.setOnClickListener(view -> onItemClickRVListener.onItemClick(categoryViewHolder.getCategory()));
    }
}