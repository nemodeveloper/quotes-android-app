package ru.nemodev.project.quotes.mvp.category.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.Category;


public class CategoryCardView extends CardView
{
    private Category category;

    public CategoryCardView(@NonNull Context context)
    {
        this(context, null);
    }

    public CategoryCardView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CategoryCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setCategory(Category category)
    {
        this.category = category;

        TextDrawable drawable = TextDrawable.builder()
                .buildRound("", ColorGenerator.MATERIAL.getColor(category.getId()));

        ImageView categoryImg = this.findViewById(R.id.categoryImg);
        categoryImg.setImageDrawable(drawable);

        TextView categoryName = this.findViewById(R.id.categoryName);
        categoryName.setText(category.getName());
    }

    public Category getCategory()
    {
        return category;
    }
}
