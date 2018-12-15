package ru.nemodev.project.quotes.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.api.dto.QuoteDTO;
import ru.nemodev.project.quotes.entity.Quote;
import ru.nemodev.project.quotes.entity.QuoteInfo;

public final class QuoteUtils
{
    private static final String QUOTE_AUTHOR_SYMBOL = "©";

    private QuoteUtils() {}

    public static String getAuthorName(QuoteInfo quote)
    {
        return quote.getAuthor() == null
                ? AndroidUtils.getTextById(R.string.author_unknown_name)
                : quote.getAuthor().getFullName();
    }

    public static String getQuoteSource(QuoteInfo quoteInfo, boolean inLine)
    {
        Quote quote = quoteInfo.getQuote();

        StringBuilder result = new StringBuilder();
        if (StringUtils.isNotBlank(quote.getSource()))
            result.append(quote.getSource());
        if (StringUtils.isNotBlank(quote.getYear()))
            result.append(inLine ? " " : "\n").append(quote.getYear());

        return result.toString();
    }

    public static String getQuoteTextForShare(QuoteInfo quoteInfo)
    {
        StringBuilder result = new StringBuilder(quoteInfo.getQuote().getText());

        String quoteAuthor = getAuthorName(quoteInfo);
        if (StringUtils.isNotBlank(quoteAuthor))
            result.append("\n\n").append(QUOTE_AUTHOR_SYMBOL).append(quoteAuthor);

        String quoteSource = getQuoteSource(quoteInfo, false);
        if (StringUtils.isNotBlank(quoteSource))
            result.append("\n").append(quoteSource);

        return result.toString();
    }

    public static List<Quote> fromQuotesDTO(List<QuoteDTO> quoteDTOList)
    {
        if (CollectionUtils.isEmpty(quoteDTOList))
            return Collections.emptyList();

        List<Quote> quotes = new ArrayList<>(quoteDTOList.size());
        for (QuoteDTO quoteDTO : quoteDTOList)
        {
            quotes.add(fromQuoteDTO(quoteDTO));
        }

        return quotes;
    }

    public static Quote fromQuoteDTO(QuoteDTO quoteDTO)
    {
        Quote quote = new Quote();
        quote.setId(quoteDTO.getId());
        quote.setAuthorId(quoteDTO.getAuthor().getId());
        quote.setCategoryId(quoteDTO.getCategory().getId());
        quote.setText(quoteDTO.getText());
        quote.setSource(quoteDTO.getSource());
        quote.setYear(quoteDTO.getYear());

        return quote;
    }

    public static List<QuoteInfo> toQuotesInfo(List<QuoteDTO> quoteDTOList)
    {
        if (CollectionUtils.isEmpty(quoteDTOList))
            return Collections.emptyList();

        List<QuoteInfo> quotes = new ArrayList<>(quoteDTOList.size());
        for (QuoteDTO quoteDTO : quoteDTOList)
        {
            quotes.add(toQuoteInfo(quoteDTO));
        }

        return quotes;
    }

    public static QuoteInfo toQuoteInfo(QuoteDTO quoteDTO)
    {
        QuoteInfo quoteInfo = new QuoteInfo();
        quoteInfo.setQuote(fromQuoteDTO(quoteDTO));
        quoteInfo.setAuthor(AuthorUtils.convertAuthor(quoteDTO.getAuthor()));
        quoteInfo.setCategory(CategoryUtils.convertCategory(quoteDTO.getCategory()));

        return quoteInfo;
    }

    public static List<Quote> fromQuotesInfo(List<QuoteInfo> quoteInfoList)
    {
        if (CollectionUtils.isEmpty(quoteInfoList))
            return Collections.emptyList();

        List<Quote> quotes = new ArrayList<>(quoteInfoList.size());
        for (QuoteInfo quoteInfo : quoteInfoList)
        {
            quotes.add(fromQuoteInfo(quoteInfo));
        }

        return quotes;
    }

    public static Quote fromQuoteInfo(QuoteInfo quoteInfo)
    {
        return quoteInfo.getQuote();
    }
}
