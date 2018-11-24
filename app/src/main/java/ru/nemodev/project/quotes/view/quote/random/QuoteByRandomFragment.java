package ru.nemodev.project.quotes.view.quote.random;

import android.view.View;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.load.LoadDataListener;
import ru.nemodev.project.quotes.core.load.LoadEventInfo;
import ru.nemodev.project.quotes.entity.external.Quote;
import ru.nemodev.project.quotes.service.quote.QuoteCacheService;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.view.base.BaseQuoteFragment;

public class QuoteByRandomFragment extends BaseQuoteFragment
{
    @Override
    protected QuoteByRandomAdapter getDataAdapter()
    {
        return new QuoteByRandomAdapter(getActivity());
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
                return QuoteCacheService.getInstance().getRandom(Collections.singletonMap("count", "200"));
            }

            @Override
            public void onFirstDataLoaded()
            {
                contentLoadingProgressBar.setVisibility(View.GONE);
            }
        };
    }

    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
        toolbar.setTitle(AndroidUtils.getTextById(R.string.random_title));
    }
}