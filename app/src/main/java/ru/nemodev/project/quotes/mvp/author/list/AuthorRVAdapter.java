package ru.nemodev.project.quotes.mvp.author.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.recyclerView.OnItemClickRVListener;
import ru.nemodev.project.quotes.core.search.FastSearchRVAdapter;
import ru.nemodev.project.quotes.entity.Author;

public class AuthorRVAdapter extends FastSearchRVAdapter<Author, AuthorRVAdapter.AuthorViewHolder>
{
    private final OnItemClickRVListener<Author> onItemClickRVListener;

    public AuthorRVAdapter(Context context, List<Author> authors, OnItemClickRVListener<Author> onItemClickRVListener)
    {
        super(context, authors);
        this.onItemClickRVListener = onItemClickRVListener;
    }

    @Override
    protected String getSearchSectionName(Author item)
    {
        return item.getFullName().substring(0, 1).toUpperCase();
    }

    @Override
    protected List<Author> getFilteredData(String rawSearch)
    {
        String searchLowerCase = rawSearch.toLowerCase();

        List<Author> filteredAuthors = new ArrayList<>();
        for (Author author : data)
        {
            if (author.getFullName().toLowerCase().contains(searchLowerCase))
            {
                filteredAuthors.add(author);
            }
        }

        return filteredAuthors;
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
    protected void doOnBindViewHolder(@NonNull AuthorViewHolder authorViewHolder, int position)
    {
        authorViewHolder.setAuthor(getItem(position));
        authorViewHolder.authorCardView.setOnClickListener(view -> onItemClickRVListener.onItemClick(authorViewHolder.getAuthor()));
    }
}