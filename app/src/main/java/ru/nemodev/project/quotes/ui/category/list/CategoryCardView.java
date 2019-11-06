package ru.nemodev.project.quotes.ui.category.list;

import android.content.Context;
import android.util.AttributeSet;
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

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.category.Category;


public class CategoryCardView extends MaterialCardView
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
        if (StringUtils.isNotEmpty(category.getImageURL()))
        {
            Glide.with(getContext())
                    .load(category.getImageURL())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(drawable)
                    .error(drawable)
                    .transform(new CircleCrop())
                    .into(categoryImg);
        }
        else
        {
            categoryImg.setImageDrawable(drawable);
        }

        categoryImg.setImageDrawable(drawable);

        TextView categoryName = this.findViewById(R.id.categoryName);
        categoryName.setText(category.getName());
    }

    public Category getCategory()
    {
        return category;
    }
}
