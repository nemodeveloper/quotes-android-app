package ru.nemodev.project.quotes.view.base;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ru.nemodev.project.quotes.R;

public abstract class BaseToolbarFragment extends Fragment
{
    protected Toolbar toolbar;
    private DrawerLayout drawer;

    protected void initToolbar(View root)
    {
        toolbar = root.findViewById(R.id.toolbar);

        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1)
        {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(view -> getActivity().getSupportFragmentManager().popBackStack());
        }
        else
        {
            // Инициализируем иконку меню
            drawer = getActivity().findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }
    }
}
