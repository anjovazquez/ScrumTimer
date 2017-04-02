package com.avv.scrumtimer;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.avv.scrumtimer.participants.view.ParticipantGroupsFragment;
import com.avv.scrumtimer.preference.ScrumPreferences;
import com.avv.scrumtimer.timer.CountDownTimerFragment;
import com.avv.scrumtimer.participants.view.ParticipantsFragment;
import com.avv.scrumtimer.results.view.ResultsChartFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ParticipantGroupsFragment.OnGroupSelectedListener, CountDownTimerFragment.OnCountdownInteractionListener, ParticipantsFragment.OnFragmentInteractionListener, MainView {

    private MainPresenter presenter;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        presenter = new MainPresenter();
        presenter.setMainView(this);
        presenter.initLoadData();

        final android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        installMenuListeners(navigationView);

        if (isParticipantGroupSelected() && hasParticipantGroupMembers()) {
            loadCountDownFragment();
        } else {
            loadParticipantGroupsFragment(false);
        }
    }

    private boolean isParticipantGroupSelected() {
        return presenter.isParticipantGroupSelected();
    }

    private boolean hasParticipantGroupMembers() {
        return presenter.hasParticipantGroupMembers();
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void installMenuListeners(NavigationView navigationView) {

        MenuItem navConfig = navigationView.getMenu().findItem(R.id.nav_config);
        navConfig.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                loadParticipantGroupsFragment(true);
                return false;
            }
        });


        MenuItem navPreferences = navigationView.getMenu().findItem(R.id.nav_prefs);
        navPreferences.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(MainActivity.this,
                        ScrumPreferences.class));
                return false;
            }

        });

        MenuItem navResults = navigationView.getMenu().findItem(R.id.nav_results);
        navResults.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                loadResultFragment();
                return false;
            }
        });

        MenuItem navTimer = navigationView.getMenu().findItem(R.id.nav_countdown);
        navTimer.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                loadCountDownFragment();
                return false;
            }
        });
    }

    private Fragment currentFragment;

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    @Override
    public void onFragmentResultLoad() {
        loadResultFragment();
    }

    private void loadCountDownFragment() {
        if (!(currentFragment instanceof CountDownTimerFragment)) {
            CountDownTimerFragment newFragment = new CountDownTimerFragment();
            Bundle args = new Bundle();
            args.putString("groupName", presenter.getSelectedGroup());
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            setCurrentFragment(newFragment);
            transaction.commit();
        }
    }

    private void loadParticipansFragment(String groupName) {
        if (!(currentFragment instanceof ParticipantsFragment)) {
            ParticipantsFragment newFragment = ParticipantsFragment.newInstance();
            Bundle args = new Bundle();
            args.putString("groupName", groupName);
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            setCurrentFragment(newFragment);
            transaction.commit();
        }
    }

    private void loadParticipantGroupsFragment(boolean backNavigation) {

        if (!(currentFragment instanceof ParticipantGroupsFragment)) {
            ParticipantGroupsFragment newFragment = new ParticipantGroupsFragment();
            Bundle args = new Bundle();
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            transaction.replace(R.id.fragment_container, newFragment);
            if (backNavigation) {
                transaction.addToBackStack(null);
            }
            setCurrentFragment(newFragment);
            transaction.commit();
        }
    }

    private void loadResultFragment() {

        if (!(currentFragment instanceof ResultsChartFragment)) {
            ResultsChartFragment newFragment = new ResultsChartFragment();
            Bundle args = new Bundle();
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            setCurrentFragment(newFragment);
            transaction.commit();
        }
    }

    @Override
    public void onGroupSelected(String groupName) {
        loadParticipansFragment(groupName);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    @Override
    public Context getContext() {
        return this;
    }
}
