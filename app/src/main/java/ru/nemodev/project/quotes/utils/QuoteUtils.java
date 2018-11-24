package ru.nemodev.project.quotes.utils;

import org.apache.commons.lang3.StringUtils;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.external.Quote;

public final class QuoteUtils
{
    private static final String QUOTE_AUTHOR_SYMBOL = "©";

    private QuoteUtils() {}

    public static String getAuthorName(Quote quote)
    {
        return quote.getAuthor() == null
                ? AndroidUtils.getTextById(R.string.author_unknown_name)
                : quote.getAuthor().getFullName();
    }

    public static String getQuoteSource(Quote quote, boolean inLine)
    {
        StringBuilder quoteInfo = new StringBuilder();
        if (StringUtils.isNotBlank(quote.getSource()))
            quoteInfo.append(quote.getSource());
        if (StringUtils.isNotBlank(quote.getYear()))
            quoteInfo.append(inLine ? " " : "\n").append(quote.getYear());

        return quoteInfo.toString();
    }

    public static String getQuoteTextForShare(Quote quote)
    {
        StringBuilder quoteInfo = new StringBuilder(quote.getText());

        String quoteAuthor = getAuthorName(quote);
        if (StringUtils.isNotBlank(quoteAuthor))
            quoteInfo.append("\n\n").append(QUOTE_AUTHOR_SYMBOL).append(quoteAuthor);

        String quoteSource = getQuoteSource(quote, false);
        if (StringUtils.isNotBlank(quoteSource))
            quoteInfo.append("\n").append(quoteSource);

        return quoteInfo.toString();
    }
}
