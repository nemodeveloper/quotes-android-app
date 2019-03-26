package ru.nemodev.project.quotes.mvp.purchase;

public enum PurchaseType
{
    QUOTE_ADB("quote_adb"),
    QUOTE_WIDGET("quote_widget");

    private final String productId;

    PurchaseType(String productId)
    {
        this.productId = productId;
    }

    public String getProductId()
    {
        return productId;
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
