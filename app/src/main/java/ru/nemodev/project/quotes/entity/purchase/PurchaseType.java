package ru.nemodev.project.quotes.entity.purchase;

public enum PurchaseType {
    QUOTE_WIDGET("quote_widget", "inapp"),
    QUOTE_ADB("quote_adb", "inapp"),
    QUOTE_DEV_TY("quote_dev_ty", "inapp");

    private final String sku;
    private final String itemType;

    PurchaseType(String sku, String itemType) {
        this.sku = sku;
        this.itemType = itemType;
    }

    public String getSku() {
        return sku;
    }

    public String getItemType() {
        return itemType;
    }

    public static PurchaseType getBySkuId(String skuId) {
        for (PurchaseType purchaseType : values()) {
            if (purchaseType.getSku().equals(skuId)) {
                return purchaseType;
            }
        }

        throw new IllegalArgumentException("Не корректный значение skuId = " + skuId);
    }
}
