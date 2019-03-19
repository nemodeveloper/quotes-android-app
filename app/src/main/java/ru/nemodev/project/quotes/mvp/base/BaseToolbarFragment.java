package ru.nemodev.project.quotes.mvp.base;

import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import ru.nemodev.project.quotes.R;
import ru.nemodev.project.quotes.mvp.MainActivity;

public abstract class BaseToolbarFragment extends Fragment
{
    private DrawerLayout drawer;
    protected Toolbar toolbar;

    protected void initToolbar(View root)
    {
        toolbar = root.findViewById(R.id.toolbar);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setSupportActionBar(toolbar);

        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1)
        {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(view -> getActivity().getSupportFragmentManager().popBackStack());
        }
        else
        {
            mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            drawer = getActivity().findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }
    }
}
