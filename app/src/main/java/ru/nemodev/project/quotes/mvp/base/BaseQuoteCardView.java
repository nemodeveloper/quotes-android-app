package ru.nemodev.project.quotes.mvp.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.database.AppDataBase;
import ru.nemodev.project.quotes.entity.Quote;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.QuoteUtils;

public class BaseQuoteCardView extends CardView
{
    protected final String TAG_LOG = this.getClass().getSimpleName();

    protected FragmentActivity fragmentActivity;
    protected QuoteInfo quote;
    protected OnLikeQuoteListener onLikeQuoteListener;

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

    public void setFragmentActivity(FragmentActivity fragmentActivity)
    {
        this.fragmentActivity = fragmentActivity;
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

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(quote.getAuthor() == null ? "?" : authorNameText.substring(0, 1),
                        ColorGenerator.MATERIAL.getColor(quote.getQuote().getId()));

        ImageView authorImage = this.findViewById(R.id.authorImg);
        authorImage.setImageDrawable(drawable);

        TextView authorName = this.findViewById(R.id.authorName);

        authorName.setText(authorNameText);
        if (quote.getAuthor() != null)
        {
            authorBlock.setOnClickListener(view ->
            {
                MainActivity mainActivity = (MainActivity) fragmentActivity;
                mainActivity.openQuoteFragment(quote.getAuthor());
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
                AndroidUtils.openShareDialog(getContext(),
                        AndroidUtils.getTextById(R.string.share_quote_dialog_title),
                        QuoteUtils.getQuoteTextForShare(quote)));

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
                    likeButton.setImageResource(R.drawable.ic_like);
                }

                @Override
                public void unLike()
                {
                    likeButton.setImageResource(R.drawable.ic_unlike);
                }
            };
        }

        likeButton.setOnClickListener(v ->
        {
            Observable.create((ObservableOnSubscribe<Quote>) emitter -> {
                Quote likedQuote = quote.getQuote();
                AppDataBase.getInstance().getQuoteDAO().like(likedQuote.getId(), !likedQuote.getLiked());
                likedQuote.setLiked(!likedQuote.getLiked());

                emitter.onNext(likedQuote);
                emitter.onComplete();
            })
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
                    Log.e(TAG_LOG, "Ошибка лайка цитаты!", e);
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
            MainActivity mainActivity = (MainActivity) fragmentActivity;
            mainActivity.openQuoteFragment(quote.getCategory());
        });
    }

    public interface OnLikeQuoteListener
    {
        void like();
        void unLike();
    }
}