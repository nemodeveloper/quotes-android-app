package ru.nemodev.project.quotes.entity;

import com.anjlab.android.iab.v3.SkuDetails;

import org.apache.commons.lang3.StringUtils;

import ru.nemodev.project.quotes.mvp.purchase.PurchaseType;

public class Purchase
{
    private final SkuDetails skuDetails;
    private final boolean isPurchase;
    private final PurchaseType purchaseType;
    private final String title;

    public Purchase(SkuDetails skuDetails, boolean isPurchase)
    {
        this.skuDetails = skuDetails;
        this.isPurchase = isPurchase;

        this.purchaseType = PurchaseType.getByProductId(skuDetails.productId);
        this.title = removeAppDescription(skuDetails.title);
    }

    public PurchaseType getPurchaseType()
    {
        return purchaseType;
    }

    public String getDescription()
    {
        return skuDetails.description;
    }

    public String getPriceText()
    {
        return skuDetails.priceText;
    }

    public boolean isPurchase()
    {
        return isPurchase;
    }

    public String getTitle()
    {
        return title;
    }

    private String removeAppDescription(String rawString)
    {
        String title = rawString;

        if (rawString.contains("("))
        {
            title = rawString.substring(0, rawString.indexOf("("));
        }

        return StringUtils.strip(title);
    }
}
