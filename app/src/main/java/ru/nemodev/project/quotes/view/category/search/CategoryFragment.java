package ru.nemodev.project.quotes.view.category.search;

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
import ru.nemodev.project.quotes.entity.external.Category;
import ru.nemodev.project.quotes.service.category.CategoryCacheService;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.view.MainActivity;
import ru.nemodev.project.quotes.view.base.BaseToolbarFragment;

public class CategoryFragment extends BaseToolbarFragment
{
    private View root;
    private CategoryLoadRV categoryLoadRV;
    private ProgressBar contentLoadingProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.category_fragmet, container, false);

        initToolbar(root);

        contentLoadingProgressBar = root.findViewById(R.id.contentLoadingProgressBar);

        initRV(root);

        return root;
    }

    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
        toolbar.setTitle((AndroidUtils.getTextById(R.string.category_title)));
    }

    private void initRV(View root)
    {
        categoryLoadRV = root.findViewById(R.id.categoryList);
        categoryLoadRV.setHasFixedSize(true);
        categoryLoadRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        categoryLoadRV.setIndexBarVisibility(false);
        categoryLoadRV.setIndexBarCornerRadius(0);
        categoryLoadRV.setIndexBarTransparentValue(0.5f);
        categoryLoadRV.setPreviewPadding(0);
        categoryLoadRV.setIndexbarMargin(0);
        categoryLoadRV.setIndexBarColor(R.color.search_bar_background);

        initRVData();
    }

    private void initRVData()
    {
        categoryLoadRV.setAdapter(new CategoryRVAdapter(getActivity(), item ->
        {
            MainActivity mainActivity1 = (MainActivity) getActivity();
            mainActivity1.openQuoteFragment(item);
        }));

        categoryLoadRV.loadData(new LoadDataListener<Category>()
        {
            @Override
            public Observable<List<Category>> getLoadObservable(LoadEventInfo loadEventInfo)
            {
                return getLoadObservable();
            }

            @Override
            public Observable<List<Category>> getLoadObservable()
            {
                return CategoryCacheService.getInstance().getAll();
            }

            @Override
            public void onFirstDataLoaded()
            {
                contentLoadingProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
