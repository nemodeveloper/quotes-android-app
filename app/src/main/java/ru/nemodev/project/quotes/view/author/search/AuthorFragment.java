package ru.nemodev.project.quotes.view.author.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import io.reactivex.Observable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.core.load.LoadDataListener;
import ru.nemodev.project.quotes.core.load.LoadEventInfo;
import ru.nemodev.project.quotes.entity.external.Author;
import ru.nemodev.project.quotes.service.author.AuthorCacheService;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.view.MainActivity;
import ru.nemodev.project.quotes.view.base.BaseToolbarFragment;

public class AuthorFragment extends BaseToolbarFragment
{
    private View root;
    private AuthorLoadRV authorLoadRV;
    private ProgressBar contentLoadingProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.author_fragmet, container, false);

        initToolbar(root);

        contentLoadingProgressBar = root.findViewById(R.id.contentLoadingProgressBar);

        initRV(root);

        return root;
    }

    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
        toolbar.setTitle(AndroidUtils.getTextById(R.string.author_title));
    }

    private void initRV(View root)
    {
        authorLoadRV = root.findViewById(R.id.authorList);
        authorLoadRV.setHasFixedSize(true);
        authorLoadRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        authorLoadRV.setIndexBarVisibility(false);
        authorLoadRV.setIndexBarCornerRadius(0);
        authorLoadRV.setIndexBarTransparentValue(0.5f);
        authorLoadRV.setPreviewPadding(0);
        authorLoadRV.setIndexbarMargin(0);
        authorLoadRV.setIndexBarColor(R.color.search_bar_background);

        initRVData();
    }

    private void initRVData()
    {
        authorLoadRV.setAdapter(new AuthorRVAdapter(getActivity(), item ->
        {
            MainActivity mainActivity1 = (MainActivity) getActivity();
            mainActivity1.openQuoteFragment(item);
        }));

        authorLoadRV.loadData(new LoadDataListener<Author>()
        {
            @Override
            public Observable<List<Author>> getLoadObservable(LoadEventInfo loadEventInfo)
            {
                return getLoadObservable();
            }

            @Override
            public Observable<List<Author>> getLoadObservable()
            {
                return AuthorCacheService.getInstance().getAll();
            }

            @Override
            public void onFirstDataLoaded()
            {
                contentLoadingProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
