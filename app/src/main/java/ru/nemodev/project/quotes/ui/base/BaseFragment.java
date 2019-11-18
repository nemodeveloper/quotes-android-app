package ru.nemodev.project.quotes.ui.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.ui.main.viewmodel.MainViewModel;
import ru.nemodev.project.quotes.utils.AndroidUtils;


public abstract class BaseFragment extends Fragment {

    protected View root;
    protected MainViewModel mainViewModel;

    protected ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(AndroidUtils.getString(R.string.load_message));

        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        return root;
    }

    protected void showLoader() {
        progressDialog.show();
    }

    protected void hideLoader() {
        progressDialog.dismiss();
    }
}
