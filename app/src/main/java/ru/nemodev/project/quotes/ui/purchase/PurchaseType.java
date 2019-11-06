package ru.nemodev.project.quotes.ui.purchase;

public enum PurchaseType
{
    QUOTE_WIDGET("quote_widget", "inapp"),
    QUOTE_ADB("quote_adb", "inapp"),
    QUOTE_DEV_TY("quote_dev_ty", "inapp");

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
