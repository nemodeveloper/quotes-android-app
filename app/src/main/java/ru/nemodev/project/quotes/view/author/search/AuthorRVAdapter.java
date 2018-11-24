package ru.nemodev.project.quotes.view.author.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.recyclerView.ItemClickRVListener;
import ru.nemodev.project.quotes.core.search.FastSearchRVAdapter;
import ru.nemodev.project.quotes.entity.external.Author;

public class AuthorRVAdapter extends FastSearchRVAdapter<Author, AuthorRVAdapter.AuthorViewHolder>
{
    private static final int INITIAL_AUTHOR_SIZE = 1000;

    private final ItemClickRVListener<Author> itemClickRVListener;

    public AuthorRVAdapter(Context context, ItemClickRVListener<Author> itemClickRVListener)
    {
        super(context, INITIAL_AUTHOR_SIZE);
        this.itemClickRVListener = itemClickRVListener;
    }

    @Override
    protected String getSearchSectionName(Author item)
    {
        return item.getFullName().substring(0, 1).toUpperCase();
    }

    public static class AuthorViewHolder extends RecyclerView.ViewHolder
    {
        private final AuthorCardView authorCardView;

        public AuthorViewHolder(AuthorCardView authorCardView)
        {
            super(authorCardView);
            this.authorCardView = authorCardView;
        }

        public void setAuthor(Author author)
        {
            this.authorCardView.setAuthor(author);
        }

        public Author getAuthor()
        {
            return authorCardView.getAuthor();
        }
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new AuthorViewHolder((AuthorCardView) LayoutInflater.from(context).inflate(R.layout.author_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position)
    {
        holder.setAuthor(getItem(position));
        holder.authorCardView.setOnClickListener(view -> itemClickRVListener.onItemClick(holder.getAuthor()));
    }
}