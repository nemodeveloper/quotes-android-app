package ru.nemodev.project.quotes.ui.base;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import org.apache.commons.lang3.StringUtils;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.app.AndroidApplication;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;


public abstract class BaseQuoteAdapter<VH extends RecyclerView.ViewHolder> extends PagedListAdapter<QuoteInfo, VH> {

    private static DiffUtil.ItemCallback<QuoteInfo> DIFF_CALLBACK = new DiffUtil.ItemCallback<QuoteInfo>() {
        @Override
        public boolean areItemsTheSame(@NonNull QuoteInfo oldItem, @NonNull QuoteInfo newItem) {
            return oldItem.getQuote().getId().equals(newItem.getQuote().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull QuoteInfo oldItem, @NonNull QuoteInfo newItem) {
            return oldItem.equals(newItem);
        }
    };

    protected final Context context;
    public final OnQuoteCardClickListener onQuoteCardClickListener;

    public BaseQuoteAdapter(Context context, OnQuoteCardClickListener onQuoteCardClickListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.onQuoteCardClickListener = onQuoteCardClickListener;
    }

    public void onLikeClick(View view, QuoteInfo quoteInfo) {
        ImageView imageView = (ImageView) view;
        imageView.setImageResource(quoteInfo.getQuote().getLiked() ? R.drawable.ic_unlike : R.drawable.ic_like);
        onQuoteCardClickListener.onLikeClick(quoteInfo);
    }

    @BindingAdapter({"authorAvatar"})
    public static void loadAuthorAvatar(ImageView imageView, Author author) {
        String authorNameText = author.getFullName();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(authorNameText.substring(0, 1),
                        ColorGenerator.MATERIAL.getColor(author.getId()));

        if (StringUtils.isNotEmpty(author.getImageURL())) {
            Glide.with(AndroidApplication.getInstance())
                    .load(author.getImageURL())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(drawable)
                    .error(drawable)
                    .transform(new CircleCrop())
                    .into(imageView);
        }
        else {
            imageView.setImageDrawable(drawable);
        }
    }
}