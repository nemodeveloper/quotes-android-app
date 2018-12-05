package ru.nemodev.project.quotes.mvp.author.list;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;

import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.entity.external.Author;
import ru.nemodev.project.quotes.mvp.MainActivity;
import ru.nemodev.project.quotes.mvp.base.BaseToolbarFragment;
import ru.nemodev.project.quotes.utils.AndroidUtils;
import ru.nemodev.project.quotes.utils.NetworkUtils;


public class AuthorListFragment extends BaseToolbarFragment implements AuthorListContract.AuthorListView
{
    private View root;
    private IndexFastScrollRecyclerView authorLoadRV;
    private ProgressBar progressBar;
    private AuthorListContract.AuthorListPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        if (root != null)
            return root;

        root = inflater.inflate(R.layout.author_fragmet, container, false);

        initToolbar(root);
        initRV(root);
        initProgressBar();

        presenter = new AuthorListPresenterImpl(this);
        presenter.loadAuthors();

        connectToNetworkEvents();

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
    }

    private void initProgressBar()
    {
        progressBar = root.findViewById(R.id.contentLoadingProgressBar);
    }

    // TODO полумать как обыграть события сети более красиво в рамках MVP
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
                            presenter.loadAuthors();
                        else
                            AndroidUtils.showToastMessage(R.string.connect_off_message);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
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
    public void showAuthors(List<Author> authors)
    {
        authorLoadRV.setAdapter(new AuthorRVAdapter(getActivity(), authors, item ->
        {
            MainActivity mainActivity1 = (MainActivity) getActivity();
            mainActivity1.openQuoteFragment(item);
        }));

        authorLoadRV.setIndexBarVisibility(true);
    }
}
