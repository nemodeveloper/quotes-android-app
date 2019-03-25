package ru.nemodev.project.quotes.mvp.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.database.AppDataBase;
import ru.nemodev.project.quotes.entity.Author;
import ru.nemodev.project.quotes.entity.Quote;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.LogUtils;
import ru.nemodev.project.quotes.utils.MetricUtils;
import ru.nemodev.project.quotes.utils.QuoteUtils;

public class BaseQuoteCardView extends CardView
{
    protected final String TAG_LOG = this.getClass().getSimpleName();

    protected QuoteInfo quote;
    protected OnLikeQuoteListener onLikeQuoteListener;
    protected OnQuoteCardClickListener onQuoteCardClickListener;

    public BaseQuoteCardView(@NonNull Context context)
    {
        super(context);
    }

    public BaseQuoteCardView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public BaseQuoteCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setOnQuoteCardClickListener(OnQuoteCardClickListener onQuoteCardClickListener)
    {
        this.onQuoteCardClickListener = onQuoteCardClickListener;
    }

    public void setQuote(QuoteInfo quote)
    {
        this.quote = quote;

        showAuthor();
        showQuote();
        showActions();
    }

    public void setOnLikeQuoteListener(OnLikeQuoteListener onLikeQuoteListener)
    {
        this.onLikeQuoteListener = onLikeQuoteListener;
    }

    protected void showAuthor()
    {
        View authorBlock = this.findViewById(R.id.authorBlock);

        String authorNameText = QuoteUtils.getAuthorName(quote);

        Author author = quote.getAuthor();
        ImageView authorImage = this.findViewById(R.id.authorImg);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(author == null ? "?" : authorNameText.substring(0, 1),
                        ColorGenerator.MATERIAL.getColor(quote.getQuote().getAuthorId()));

        if (author != null && StringUtils.isNotEmpty(author.getImageURL()))
        {
            Glide.with(getContext())
                    .load(author.getImageURL())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(drawable)
                    .error(drawable)
                    .transform(new CircleCrop())
                    .into(authorImage);
        }
        else
        {
            authorImage.setImageDrawable(drawable);
        }

        TextView authorName = this.findViewById(R.id.authorName);
        authorName.setText(authorNameText);
        if (quote.getAuthor() != null)
        {
            authorBlock.setOnClickListener(view ->
            {
                MetricUtils.viewEvent(MetricUtils.ViewType.AUTHOR_QUOTES_FROM_QUOTE_CARD);
                onQuoteCardClickListener.onAuthorClick(quote.getAuthor());
            });
        }

        String quoteSourceText = QuoteUtils.getQuoteSource(quote, true);
        TextView quoteSource = this.findViewById(R.id.quoteSource);
        if (StringUtils.isNoneBlank(quoteSourceText))
        {
            quoteSource.setText(quoteSourceText);
        }
        else
        {
            quoteSource.setVisibility(GONE);
        }
    }

    protected void showQuote()
    {
        TextView quoteText = this.findViewById(R.id.quoteText);
        quoteText.setText(quote.getQuote().getText());
    }

    protected void showActions()
    {
        showCategoryAction();

        ImageView shareButton = findViewById(R.id.shareQuote);
        shareButton.setOnClickListener(v ->
        {
            MetricUtils.shareEvent(MetricUtils.ShareType.QUOTE);
            AndroidUtils.openShareDialog(getContext(),
                    AndroidUtils.getString(R.string.share_quote_dialog_title),
                    QuoteUtils.getQuoteTextForShare(quote));
        });

        final ImageView likeButton = findViewById(R.id.likeQuote);
        if (quote.getQuote().getLiked())
            likeButton.setImageResource(R.drawable.ic_like);
        else
            likeButton.setImageResource(R.drawable.ic_unlike);

        if (onLikeQuoteListener == null)
        {
            onLikeQuoteListener = new OnLikeQuoteListener()
            {
                @Override
                public void like()
                {
                    MetricUtils.rateEvent(MetricUtils.RateType.QUOTE_LIKE);
                    likeButton.setImageResource(R.drawable.ic_like);
                }

                @Override
                public void unLike()
                {
                    MetricUtils.rateEvent(MetricUtils.RateType.QUOTE_UNLIKE);
                    likeButton.setImageResource(R.drawable.ic_unlike);
                }
            };
        }

        likeButton.setOnClickListener(v ->
        {
            Quote likeQuote = quote.getQuote();
            likeQuote.setLiked(!likeQuote.getLiked());
            if (likeQuote.getLiked())
            {
                likeQuote.setLikeDate(Calendar.getInstance());
            }
            else
            {
                likeQuote.setLikeDate(null);
            }

            AppDataBase.getInstance().getQuoteDAO().likeAsync(likeQuote)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Quote>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Quote quote)
                    {
                        if (quote.getLiked())
                            onLikeQuoteListener.like();
                        else
                            onLikeQuoteListener.unLike();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        LogUtils.logWithReport(TAG_LOG, "Ошибка лайка цитаты!", e);
                        // TODO показывать нужно SnackBar
                        AndroidUtils.showToastMessage(R.string.quote_like_error);
                    }

                    @Override
                    public void onComplete() { }
                });
        });
    }

    protected void showCategoryAction()
    {
        Button quoteCategoryButton = this.findViewById(R.id.quoteCategoryButton);
        quoteCategoryButton.setText(quote.getCategory().getName());
        quoteCategoryButton.setOnClickListener(view ->
        {
            MetricUtils.viewEvent(MetricUtils.ViewType.CATEGORY_QUOTES_FROM_QUOTE_CARD);
            onQuoteCardClickListener.onCategoryClick(quote.getCategory());
        });
    }

    public interface OnLikeQuoteListener
    {
        void like();
        void unLike();
    }
}