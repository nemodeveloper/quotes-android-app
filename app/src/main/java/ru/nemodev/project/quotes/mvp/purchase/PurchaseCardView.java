package ru.nemodev.project.quotes.mvp.purchase;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.Purchase;
import ru.nemodev.project.quotes.utils.AndroidUtils;

public class PurchaseCardView extends MaterialCardView
{
    private Purchase purchase;

    @BindView(R.id.purchaseImg)
    ImageView purchaseImageView;
    @BindView(R.id.purchaseTitle)
    TextView purchaseTitle;
    @BindView(R.id.purchaseDescription)
    TextView purchaseDescription;
    @BindView(R.id.purchasePrice)
    TextView purchasePrice;
    @BindView(R.id.purchaseEnableView)
    TextView purchaseEnableView;

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

    public void setPurchase(Purchase purchase)
    {
        this.purchase = purchase;
        ButterKnife.bind(this);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound("", ColorGenerator.MATERIAL.getColor(purchase.getPurchaseType().getProductId()));

        purchaseImageView.setImageDrawable(drawable);
        purchaseTitle.setText(purchase.getTitle());
        purchaseDescription.setText(purchase.getDescription());

        if (purchase.isPurchase())
            purchasePrice.setVisibility(View.GONE);
        else
            purchasePrice.setText(purchase.getPriceText());

        purchaseEnableView.setText(purchase.isPurchase()
                ? AndroidUtils.getString(R.string.purchase_already_do)
                : AndroidUtils.getString(R.string.purchase_ready));
    }
}
