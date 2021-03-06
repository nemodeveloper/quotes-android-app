package ru.nemodev.project.quotes.entity.author;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.repository.api.dto.AuthorDTO;

public final class AuthorUtils
{
    private AuthorUtils() { }

    public static List<Author> convertAuthors(List<AuthorDTO> authorDTOList)
    {
        if (CollectionUtils.isEmpty(authorDTOList))
            return Collections.emptyList();

        List<Author> authors = new ArrayList<>(authorDTOList.size());
        for (AuthorDTO authorDTO : authorDTOList)
        {
            authors.add(convertAuthor(authorDTO));
        }

        return authors;
    }

    public static Author convertAuthor(AuthorDTO authorDTO)
    {
        Author author = new Author();
        author.setId(authorDTO.getId());
        author.setFullName(authorDTO.getFullName());
        author.setImageURL(authorDTO.getImageURL());

        return author;
    }

    public static List<Author> getAuthors(List<QuoteInfo> quoteInfoList)
    {
        Map<Long, Author> authorMap = new HashMap<>();
        for (QuoteInfo quoteInfo : quoteInfoList)
        {
            authorMap.put(quoteInfo.getAuthor().getId(), quoteInfo.getAuthor());
        }

        return new ArrayList<>(authorMap.values());
    }

}
