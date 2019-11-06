package ru.nemodev.project.quotes.ui.category.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.recyclerView.OnItemClickRVListener;
import ru.nemodev.project.quotes.entity.category.Category;

public class CategoryListAdapter extends PagedListAdapter<Category, CategoryListAdapter.CategoryViewHolder> {

    private static DiffUtil.ItemCallback<Category> DIFF_CALLBACK = new DiffUtil.ItemCallback<Category>() {
        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };

    private final Context context;
    private final OnItemClickRVListener<Category> onItemClickRVListener;

    public CategoryListAdapter(Context context, OnItemClickRVListener<Category> onItemClickRVListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.onItemClickRVListener = onItemClickRVListener;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final CategoryCardView categoryCardView;

        public CategoryViewHolder(CategoryCardView categoryCardView) {
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
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder((CategoryCardView) LayoutInflater.from(context).inflate(R.layout.category_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int position) {
        categoryViewHolder.setCategory(getItem(position));
        categoryViewHolder.categoryCardView.setOnClickListener(view -> onItemClickRVListener.onItemClick(categoryViewHolder.getCategory()));
    }
}