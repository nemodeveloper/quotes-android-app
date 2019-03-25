package ru.nemodev.project.quotes.mvp.purchase;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class CompositeBillingHandler implements BillingProcessor.IBillingHandler
{
    private final List<BillingProcessor.IBillingHandler> handlers;

    public CompositeBillingHandler(List<BillingProcessor.IBillingHandler> handlers)
    {
        this.handlers = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(handlers))
        {
            this.handlers.addAll(handlers);
        }
    }

    public void addHandler(BillingProcessor.IBillingHandler handler)
    {
        if (!handlers.contains(handler))
        {
            handlers.add(handler);
        }
    }

    public void removeHandler(BillingProcessor.IBillingHandler handler)
    {
        handlers.remove(handler);
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details)
    {
        for (BillingProcessor.IBillingHandler handler : handlers)
        {
            handler.onProductPurchased(productId, details);
        }
    }

    @Override
    public void onPurchaseHistoryRestored()
    {
        for (BillingProcessor.IBillingHandler handler : handlers)
        {
            handler.onPurchaseHistoryRestored();
        }
    }

    @Override
    public void onBillingError(int errorCode, Throwable error)
    {
        for (BillingProcessor.IBillingHandler handler : handlers)
        {
            handler.onBillingError(errorCode, error);
        }
    }

    @Override
    public void onBillingInitialized()
    {
        for (BillingProcessor.IBillingHandler handler : handlers)
        {
            handler.onBillingInitialized();
        }
    }
}
