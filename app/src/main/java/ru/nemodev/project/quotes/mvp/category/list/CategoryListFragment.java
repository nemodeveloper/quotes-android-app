package ru.nemodev.project.quotes.mvp.category.list;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.Category;
import ru.nemodev.project.quotes.mvp.MainActivity;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;

public class CategoryListFragment extends BaseToolbarFragment implements CategoryListContract.CategoryListView
{
    private View root;
    private IndexFastScrollRecyclerView categoryLoadRV;
    private ProgressBar progressBar;
    private CategoryListContract.CategoryListPresenter presenter;

    private Disposable internetEventsDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.category_fragmet, container, false);

        initToolbar(root);
        initRV(root);
        initProgressBar();

        presenter = new CategoryListPresenterImpl(this);
        presenter.loadCategory();

        connectToNetworkEvents();

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
    }

    private void initProgressBar()
    {
        progressBar = root.findViewById(R.id.contentLoadingProgressBar);
    }

    private void connectToNetworkEvents()
    {
        disconnectFromNetworkEvents();
        internetEventsDisposable = NetworkUtils.getNetworkObservable()
                .subscribe(connectivity ->
                {
                    if (connectivity.state() == NetworkInfo.State.CONNECTED)
                        presenter.loadCategory();
                    else
                        AndroidUtils.showToastMessage(R.string.not_full_categories_message);
                });
    }

    private void disconnectFromNetworkEvents()
    {
        if (internetEventsDisposable != null && !internetEventsDisposable.isDisposed())
            internetEventsDisposable.dispose();
    }

    @Override
    public void showLoader()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader()
    {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showCategories(List<Category> categories)
    {
        if (CollectionUtils.isNotEmpty(categories))
        {
            categoryLoadRV.setAdapter(new CategoryListAdapter(getActivity(), categories, item ->
            {
                MainActivity mainActivity1 = (MainActivity) getActivity();
                mainActivity1.openQuoteFragment(item);
            }));
            categoryLoadRV.setIndexBarVisibility(true);
        }
    }

    @Override
    public void onDestroy()
    {
        disconnectFromNetworkEvents();
        super.onDestroy();
    }

    @Override
    public void onPause()
    {
        disconnectFromNetworkEvents();
        super.onPause();
    }

    @Override
    public void onResume()
    {
        connectToNetworkEvents();
        super.onResume();
    }
}
