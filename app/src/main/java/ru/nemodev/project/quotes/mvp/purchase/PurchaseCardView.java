package ru.nemodev.project.quotes.mvp.purchase;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
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

public class PurchaseCardView extends MaterialCardView
{
    private Purchase purchase;

    @BindView(R.id.purchaseImg)
    ImageView purchaseImageView;
    @BindView(R.id.purchaseTitle)
    TextView purchaseTitle;
    @BindView(R.id.purchasePrice)
    Button purchaseButton;

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

    public void setPurchase(Purchase purchase, OnPurchaseClickListener onPurchaseClickListener)
    {
        this.purchase = purchase;
        ButterKnife.bind(this);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound("", ColorGenerator.MATERIAL.getColor(purchase.getPurchaseType().getProductId()));

        purchaseImageView.setImageDrawable(drawable);
        purchaseTitle.setText(purchase.getTitle());

        if (purchase.isPurchase())
        {
            purchaseButton.setVisibility(View.GONE);
            this.setOnClickListener((view) -> onPurchaseClickListener.onPurchaseClick(purchase));
        }
        else
        {
            purchaseButton.setText(purchase.getPriceText());
            purchaseButton.setOnClickListener((button) -> onPurchaseClickListener.onPurchaseClick(purchase));
        }
    }
}
