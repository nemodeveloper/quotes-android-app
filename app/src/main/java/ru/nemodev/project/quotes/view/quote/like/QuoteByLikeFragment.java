package ru.nemodev.project.quotes.view.quote.like;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import io.reactivex.Observable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.load.LoadDataListener;
import ru.nemodev.project.quotes.core.load.LoadEventInfo;
import ru.nemodev.project.quotes.core.load.simple.SimpleLoadRVAdapter;
import ru.nemodev.project.quotes.entity.external.Quote;
import ru.nemodev.project.quotes.service.quote.QuoteCacheService;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.view.base.BaseQuoteFragment;

public class QuoteByLikeFragment extends BaseQuoteFragment
{
    @Override
    protected SimpleLoadRVAdapter<Quote, ? extends RecyclerView.ViewHolder> getDataAdapter()
    {
        return new QuoteByLikeAdapter(getActivity());
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
                return QuoteCacheService.getInstance().getLiked();
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

    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
        toolbar.setTitle(AndroidUtils.getTextById(R.string.like_title));
    }
}