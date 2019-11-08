package ru.nemodev.project.quotes.ui.base;

import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import butterknife.BindView;
import ru.nemodev.project.quotes.R;

public abstract class BaseFragment extends Fragment {

    @BindView(R.id.contentLoadingProgressBar) ProgressBar progressBar;

    protected void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void hideLoader() {
        progressBar.setVisibility(View.GONE);
    }
}
