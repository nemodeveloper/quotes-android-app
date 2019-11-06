package ru.nemodev.project.quotes.ui.author.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.recyclerView.OnItemClickRVListener;
import ru.nemodev.project.quotes.entity.author.Author;

public class AuthorListAdapter extends PagedListAdapter<Author, AuthorListAdapter.AuthorViewHolder> {
    private static DiffUtil.ItemCallback<Author> DIFF_CALLBACK = new DiffUtil.ItemCallback<Author>() {
        @Override
        public boolean areItemsTheSame(@NonNull Author oldItem, @NonNull Author newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Author oldItem, @NonNull Author newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };

    private final Context context;
    private final OnItemClickRVListener<Author> onItemClickRVListener;

    public AuthorListAdapter(Context context, OnItemClickRVListener<Author> onItemClickRVListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.onItemClickRVListener = onItemClickRVListener;
    }

    public static class AuthorViewHolder extends RecyclerView.ViewHolder {
        private final AuthorCardView authorCardView;

        public AuthorViewHolder(AuthorCardView authorCardView) {
            super(authorCardView);
            this.authorCardView = authorCardView;
        }

        public void setAuthor(Author author) {
            this.authorCardView.setAuthor(author);
        }

        public Author getAuthor() {
            return authorCardView.getAuthor();
        }
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AuthorViewHolder((AuthorCardView) LayoutInflater.from(context).inflate(R.layout.author_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder authorViewHolder, int position) {
        authorViewHolder.setAuthor(getItem(position));
        authorViewHolder.authorCardView.setOnClickListener(view -> onItemClickRVListener.onItemClick(authorViewHolder.getAuthor()));
    }
}