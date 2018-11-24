package ru.nemodev.project.quotes.view.quote.author;

import android.view.View;

import java.util.List;

import io.reactivex.Observable;
import ru.nemodev.project.quotes.core.load.LoadDataListener;
import ru.nemodev.project.quotes.core.load.LoadEventInfo;
import ru.nemodev.project.quotes.entity.external.Quote;
import ru.nemodev.project.quotes.service.quote.QuoteCacheService;
import ru.nemodev.project.quotes.view.base.BaseQuoteFragment;

public class QuoteByAuthorFragment extends BaseQuoteFragment
{
    public static final String AUTHOR_ID_KEY = "authorId";
    public static final String AUTHOR_NAME_KEY = "authorName";

    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
        toolbar.setTitle(getArguments().getString(AUTHOR_NAME_KEY));
    }

    @Override
    protected QuoteByAuthorAdapter getDataAdapter()
    {
        return new QuoteByAuthorAdapter(getActivity());
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
                return QuoteCacheService.getInstance().getByAuthor(getArguments().getLong(AUTHOR_ID_KEY));
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
