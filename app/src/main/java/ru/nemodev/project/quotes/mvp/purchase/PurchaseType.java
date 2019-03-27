package ru.nemodev.project.quotes.mvp.purchase;

public enum PurchaseType
{
    QUOTE_ADB("quote_adb", "inapp"),
    QUOTE_WIDGET("quote_widget", "inapp");

    private final String productId;
    private final String itemType;

    PurchaseType(String productId, String itemType)
    {
        this.productId = productId;
        this.itemType = itemType;
    }

    public String getProductId()
    {
        return productId;
    }

    public String getItemType()
    {
        return itemType;
    }

    public static PurchaseType getByProductId(String rawValue)
    {
        for (PurchaseType purchaseType : values())
        {
            if (purchaseType.getProductId().equals(rawValue))
            {
                return purchaseType;
            }
        }

        throw new IllegalArgumentException("Не корректный значение rawValue = " + rawValue);
    }
}
