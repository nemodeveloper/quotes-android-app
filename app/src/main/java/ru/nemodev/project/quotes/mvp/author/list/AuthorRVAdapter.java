package ru.nemodev.project.quotes.mvp.author.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.recyclerView.FastSearchRVAdapter;
import ru.nemodev.project.quotes.core.recyclerView.OnItemClickRVListener;
import ru.nemodev.project.quotes.entity.author.Author;

public class AuthorRVAdapter extends FastSearchRVAdapter<Author, AuthorRVAdapter.AuthorViewHolder>
{
    private final OnItemClickRVListener<Author> onItemClickRVListener;

    public AuthorRVAdapter(Context context, List<Author> authors, OnItemClickRVListener<Author> onItemClickRVListener)
    {
        super(context, authors);
        this.onItemClickRVListener = onItemClickRVListener;
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
        return new AuthorViewHolder((AuthorCardView) LayoutInflater.from(context).inflate(R.layout.author_card_view, parent, false));
    }

    @Override
    protected void doOnBindViewHolder(@NonNull AuthorViewHolder authorViewHolder, int position)
    {
        authorViewHolder.setAuthor(getItem(position));
        authorViewHolder.authorCardView.setOnClickListener(view -> onItemClickRVListener.onItemClick(authorViewHolder.getAuthor()));
    }
}