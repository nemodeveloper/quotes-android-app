package ru.nemodev.project.quotes.view.quote.category;

import android.view.View;

import java.util.List;

import io.reactivex.Observable;
import ru.nemodev.project.quotes.core.load.LoadDataListener;
import ru.nemodev.project.quotes.core.load.LoadEventInfo;
import ru.nemodev.project.quotes.entity.external.Quote;
import ru.nemodev.project.quotes.service.quote.QuoteCacheService;
import ru.nemodev.project.quotes.view.base.BaseQuoteFragment;

public class QuoteByCategoryFragment extends BaseQuoteFragment
{
    public static final String CATEGORY_ID_KEY = "categoryId";
    public static final String CATEGORY_NAME_KEY = "categoryName";

    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
        toolbar.setTitle(getArguments().getString(CATEGORY_NAME_KEY));
    }

    @Override
    protected QuoteByCategoryAdapter getDataAdapter()
    {
        return new QuoteByCategoryAdapter(getActivity());
    }

    @Override
    protected LoadDataListener<Quote> getQuoteLoadListener()
    {
        return new LoadDataListener<Quote>()
        {
            @Override
            public Observable<List<Quote>> getLoadObservable(LoadEventInfo loadEventInfo)
            {
                return getLoadObservable();
            }

            @Override
            public Observable<List<Quote>> getLoadObservable()
            {
                return QuoteCacheService.getInstance().getByCategory(getArguments().getLong(CATEGORY_ID_KEY));
            }

            @Override
            public boolean isInfinitySource()
            {
                return false;
            }

            @Override
            public void onFirstDataLoaded()
            {
                contentLoadingProgressBar.setVisibility(View.GONE);
            }
        };
    }
}