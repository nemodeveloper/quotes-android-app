package ru.nemodev.project.quotes.mvp.purchase;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.anjlab.android.iab.v3.SkuDetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nemodev.project.quotes.R;

public class PurchaseCardView extends CardView
{
    private SkuDetails skuDetails;

    @BindView(R.id.skuImg)
    ImageView skuImageView;
    @BindView(R.id.skuTitle)
    TextView skuTitle;
    @BindView(R.id.skuDescription)
    TextView skuDescription;
    @BindView(R.id.skuPrice)
    TextView skuPrice;

    public PurchaseCardView(@NonNull Context context)
    {
        super(context);
    }

    public PurchaseCardView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PurchaseCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setSkuDetails(SkuDetails skuDetails)
    {
        this.skuDetails = skuDetails;
        ButterKnife.bind(this);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound("", ColorGenerator.MATERIAL.getColor(skuDetails.productId));

        skuImageView.setImageDrawable(drawable);
        skuTitle.setText(removeAppDescription(skuDetails.title));
        skuDescription.setText(skuDetails.description);
        skuPrice.setText(skuDetails.priceText);
    }

    private String removeAppDescription(String rawString)
    {
        if (rawString.contains("("))
        {
            return rawString.substring(0, rawString.indexOf("("));
        }

        return rawString;
    }
}
