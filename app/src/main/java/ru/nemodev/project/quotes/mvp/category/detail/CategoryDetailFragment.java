package ru.nemodev.project.quotes.mvp.category.detail;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.QuoteInfo;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class CategoryDetailFragment extends BaseToolbarFragment implements CategoryDetailContract.CategoryDetailView
{
    public static final String CATEGORY_ID_KEY = "categoryId";
    public static final String CATEGORY_NAME_KEY = "categoryName";

    private View root;
    private RecyclerView quoteRV;
    private ProgressBar progressBar;
    private CategoryDetailContract.CategoryDetailPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.base_quote_fragmet, container, false);

        initToolbar(root);
        initRV();
        initProgressBar();

        presenter = new CategoryDetailPresenterImpl(getArguments().getLong(CATEGORY_ID_KEY), this);
        presenter.loadQuotes();

        connectToNetworkEvents();

        return root;
    }

    private void initRV()
    {
        quoteRV = root.findViewById(R.id.quoteList);
        quoteRV.setHasFixedSize(true);
        quoteRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initProgressBar()
    {
        progressBar = root.findViewById(R.id.contentLoadingProgressBar);
    }

    private void connectToNetworkEvents()
    {
        NetworkUtils.getNetworkObservable()
                .subscribe(new Observer<Connectivity>()
                {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Connectivity connectivity)
                    {
                        if (connectivity.state() == NetworkInfo.State.CONNECTED)
                            presenter.loadQuotes();
                        else
                            AndroidUtils.showToastMessage(R.string.connect_off_message);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }


    // TODO переписать чтобы эту логику делал presenter
    @Override
    protected void initToolbar(View root)
    {
        super.initToolbar(root);
        toolbar.setTitle(getArguments().getString(CATEGORY_NAME_KEY));
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
    public void showQuotes(List<QuoteInfo> quotes)
    {
        // TODO прокидывать OnClickQuoteActionListener вместо Context
        quoteRV.setAdapter(new CategoryQuotesAdapter(getActivity(), quotes));
    }
}