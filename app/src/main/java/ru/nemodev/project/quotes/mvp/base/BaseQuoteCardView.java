package ru.nemodev.project.quotes.mvp.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.card.MaterialCardView;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nemodev.core.app.AndroidApplication;
import ru.nemodev.core.utils.AndroidUtils;
import ru.nemodev.core.utils.LogUtils;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.author.Author;
import ru.nemodev.project.quotes.entity.quote.Quote;
import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.entity.quote.QuoteUtils;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;
import ru.nemodev.project.quotes.utils.MetricUtils;
import ru.nemodev.project.quotes.widget.QuoteWidgetProvider;


public class BaseQuoteCardView extends MaterialCardView
{
    protected final String TAG_LOG = this.getClass().getSimpleName();

    protected QuoteInfo quote;
    protected OnLikeQuoteListener onLikeQuoteListener;
    protected OnQuoteCardClickListener onQuoteCardClickListener;

    @Nullable
    @BindView(R.id.authorBlock)
    View authorBlock;
    @Nullable
    @BindView(R.id.authorImg)
    ImageView authorImg;
    @Nullable
    @BindView(R.id.authorName)
    TextView authorName;
    @Nullable
    @BindView(R.id.quoteSource)
    TextView quoteSource;

    @BindView(R.id.quoteText)
    TextView quoteText;

    @Nullable
    @BindView(R.id.quoteCategoryButton)
    Button quoteCategoryButton;
    @BindView(R.id.likeQuote)
    ImageView likeQuote;
    @BindView(R.id.quoteToWidget)
    ImageView quoteToWidget;
    @BindView(R.id.shareQuote)
    ImageView shareQuote;

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
        ButterKnife.bind(this);
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
        String authorNameText = QuoteUtils.getAuthorName(quote);
        Author author = quote.getAuthor();

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
                    .into(authorImg);
        }
        else
        {
            authorImg.setImageDrawable(drawable);
        }

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
        if (StringUtils.isNoneBlank(quoteSourceText))
        {
            quoteSource.setText(quoteSourceText);
        }
        else
        {
            quoteSource.setVisibility(GONE);
        }
    }

    private void showQuote()
    {
        quoteText.setText(quote.getQuote().getText());
    }

    private void showActions()
    {
        initCategoryAction();
        initLikeAction();
        initQuoteToWidgetAction();
        initShareQuoteAction();
    }

    private void initLikeAction()
    {
        if (quote.getQuote().getLiked())
            likeQuote.setImageResource(R.drawable.ic_like);
        else
            likeQuote.setImageResource(R.drawable.ic_unlike);

        if (onLikeQuoteListener == null)
        {
            onLikeQuoteListener = new OnLikeQuoteListener()
            {
                @Override
                public void like()
                {
                    MetricUtils.rateEvent(MetricUtils.RateType.QUOTE_LIKE);
                    likeQuote.setImageResource(R.drawable.ic_like);
                }

                @Override
                public void unLike()
                {
                    MetricUtils.rateEvent(MetricUtils.RateType.QUOTE_UNLIKE);
                    likeQuote.setImageResource(R.drawable.ic_unlike);
                }
            };
        }

        likeQuote.setOnClickListener(v ->
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

            View rootView = getRootView().findViewById(R.id.viewContainer);
            AppDataBase.getInstance().getQuoteRepository().likeAsync(likeQuote)
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
                            LogUtils.error(TAG_LOG, "Ошибка лайка цитаты!", e);
                            AndroidUtils.showSnackBarMessage(rootView, R.string.quote_like_error);
                        }

                        @Override
                        public void onComplete() { }
                    });
        });
    }

    private void initQuoteToWidgetAction()
    {
        if (AndroidUtils.addWidgetFromAppSupported(getContext()))
        {
            quoteToWidget.setOnClickListener(v ->
            {
                MetricUtils.viewEvent(MetricUtils.ViewType.QUOTE_TO_WIDGET);
                AndroidApplication.getInstance().getAppSetting().setLong(QuoteWidgetProvider.QUOTE_ID_BUNDLE_KEY, quote.getQuote().getId());
                AndroidUtils.openAddWidgetDialog(getContext(), QuoteWidgetProvider.class);
            });
        }
        else
        {
            quoteToWidget.setVisibility(View.GONE);
        }
    }

    private void initShareQuoteAction()
    {
        shareQuote.setOnClickListener(v ->
        {
            MetricUtils.shareEvent(MetricUtils.ShareType.QUOTE);
            AndroidUtils.openShareDialog(getContext(),
                    AndroidUtils.getString(R.string.share_quote_dialog_title),
                    QuoteUtils.getQuoteTextForShare(quote));
        });
    }

    protected void initCategoryAction()
    {
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