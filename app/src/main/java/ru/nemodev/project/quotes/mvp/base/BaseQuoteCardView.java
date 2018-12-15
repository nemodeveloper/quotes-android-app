package ru.nemodev.project.quotes.mvp.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.apache.commons.lang3.StringUtils;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.MainActivity;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.QuoteUtils;

public class BaseQuoteCardView extends CardView
{
    protected FragmentActivity fragmentActivity;
    protected QuoteInfo quote;

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
}