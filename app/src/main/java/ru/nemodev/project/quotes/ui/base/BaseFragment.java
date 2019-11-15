package ru.nemodev.project.quotes.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.main.viewmodel.MainViewModel;

public abstract class BaseFragment extends Fragment {

    protected View root;
    protected MainViewModel mainViewModel;

    protected ProgressBar progressBar;

    public BaseFragment() {
        super();
    }

    public BaseFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = super.onCreateView(inflater, container, savedInstanceState);
        // TODO создавать программно
        progressBar = root.findViewById(R.id.contentLoadingProgressBar);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        return root;
    }

    protected void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void hideLoader() {
        progressBar.setVisibility(View.GONE);
    }
}
