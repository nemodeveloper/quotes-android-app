package ru.nemodev.project.quotes.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.nemodev.project.quotes.api.dto.CategoryDTO;
import ru.nemodev.project.quotes.entity.Category;
import ru.nemodev.project.quotes.entity.QuoteInfo;

public final class CategoryUtils
{
    private CategoryUtils() { }

    public static List<Category> convertCategories(List<CategoryDTO> categoryDTOList)
    {
        if (CollectionUtils.isEmpty(categoryDTOList))
            return Collections.emptyList();

        List<Category> categories = new ArrayList<>(categoryDTOList.size());
        for (CategoryDTO categoryDTO : categoryDTOList)
        {
            categories.add(convertCategory(categoryDTO));
        }

        return categories;
    }

    public static Category convertCategory(CategoryDTO categoryDTO)
    {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setImageURL(categoryDTO.getImageURL());

        return category;
    }

    public static List<Category> getCategories(List<QuoteInfo> quoteInfoList)
    {
        Map<Long, Category> authorMap = new HashMap<>();
        for (QuoteInfo quoteInfo : quoteInfoList)
        {
            authorMap.put(quoteInfo.getCategory().getId(), quoteInfo.getCategory());
        }

        return new ArrayList<>(authorMap.values());
    }
}
