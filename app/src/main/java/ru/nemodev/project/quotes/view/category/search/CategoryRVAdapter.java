package ru.nemodev.project.quotes.view.category.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.recyclerView.ItemClickRVListener;
import ru.nemodev.project.quotes.core.search.FastSearchRVAdapter;
import ru.nemodev.project.quotes.entity.external.Category;

public class CategoryRVAdapter extends FastSearchRVAdapter<Category, CategoryRVAdapter.CategoryViewHolder>
{
    private static final int INITIAL_CATEGORY_SIZE = 2000;

    private final ItemClickRVListener<Category> itemClickRVListener;

    public CategoryRVAdapter(Context context, ItemClickRVListener<Category> itemClickRVListener)
    {
        super(context, INITIAL_CATEGORY_SIZE);
        this.itemClickRVListener = itemClickRVListener;
    }

    @Override
    protected String getSearchSectionName(Category item)
    {
        return item.getName().substring(0, 1).toUpperCase();
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
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position)
    {
        holder.setCategory(getItem(position));
        holder.categoryCardView.setOnClickListener(view -> itemClickRVListener.onItemClick(holder.getCategory()));
    }
}