package ru.nemodev.project.quotes.mvp.purchase;

public enum PurchaseType
{
    QUOTE_ADB("quote_adb"),
    QUOTE_WIDGET("quote_widget");

    private final String skuName;

    PurchaseType(String skuName)
    {
        this.skuName = skuName;
    }

    public String getSkuName()
    {
        return skuName;
    }

    public static PurchaseType getBySkuName(String rawValue)
    {
        for (PurchaseType purchaseType : values())
        {
            if (purchaseType.getSkuName().equals(rawValue))
            {
                return purchaseType;
            }
        }

        throw new IllegalArgumentException("Не корректный значение rawValue = " + rawValue);
    }
}
