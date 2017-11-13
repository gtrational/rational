package edu.gatech.cs2340.gtrational.rational.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.cs2340.gtrational.rational.Callbacks;
import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.model.Model;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

/**
 * Class to handle the main dashboard activity
 */
public class MainDashboardActivity
        extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static class FragInfo {
        final Class<? extends Fragment> fragmentClass;
        final String title;

        FragInfo(Class<? extends Fragment> fragmentClass, String title) {
            this.fragmentClass = fragmentClass;
            this.title = title;
        }
    }

    private final Map<Integer, FragInfo> fragments;
    private Fragment activeFragment;
    private final List<Callbacks.VoidCallback> onDestroy;

    /**
     * Constructor for the main dashboard activity
     */
    public MainDashboardActivity() {
        //Init Fragments
        fragments = new HashMap<>();
        fragments.put(R.id.nav_dashboard, new FragInfo(DashboardFragment.class, "Dashboard"));
        fragments.put(R.id.nav_graphs, new FragInfo(GraphFragment.class, "Graphs"));
        fragments.put(R.id.nav_map, new FragInfo(MapFragment.class, "Map"));
        fragments.put(R.id.nav_sightings, new FragInfo(ListFragment.class, "All Sightings"));

        onDestroy = new ArrayList<>();
    }

    private void setFragment(int id) {
        if (!fragments.containsKey(id)) {
            return;
        }

        try {
            FragInfo info = fragments.get(id);
            setTitle(info.title);
            Fragment fragment = info.fragmentClass.newInstance();
            FragmentManager fragmentManager = getFragmentManager();
            invalidateOptionsMenu();
            fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit();
            activeFragment = fragment;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the pins of rat data for a given date range on the map
     *
     * @param start start date for rat data
     * @param end end date for rat data
     */
    public void setMapPins(long start, long end) {
        Model.getInstance().getDateRangeRatsData(start, end, (List<WebAPI.RatData> ratData) -> {
            if (isActiveFragment(MapFragment.class)) {
                ((MapFragment)activeFragment).setMapPins(ratData);
            }
        });
    }

    private boolean isActiveFragment(Class<?> clazz) {
        return activeFragment.getClass() == clazz;
    }

    /**
     * Sets the data to be displayed on the graph in the GraphFragment
     *
     * @param start the start date for rat data history
     * @param end the end date for rat data history
     * @param byYear whether to display by year or by month
     */
    public void setGraphData(long start, long end, boolean byYear) {
        Model.getInstance().getDateRangeRatsData(start, end, (Iterable<WebAPI.RatData> ratData) -> {
            Log.w("Dashboard", "Got Here");
            if (isActiveFragment(GraphFragment.class)) {
                ((GraphFragment) activeFragment).setGraphData(ratData, start, end, byYear);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Subscribe to update
        onDestroy.add(Model.getInstance().registerListener(
                Model.RAT_SIGHTING_UPDATE,
                (JSONObject updateInfo) -> {
                    if (isActiveFragment(ListFragment.class)) {
                        try {
                            ((ListFragment) activeFragment).onRatUpdate(
                                    new WebAPI.RatData(updateInfo)
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
        }));

        setContentView(R.layout.activity_main_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Generate floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
            Intent intent = new Intent(MainDashboardActivity.this, NewSightingActivity.class);
            startActivityForResult(intent, 0);
        });

        // Generate navigation drawer.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Sets Dashboard as default fragment when launched.
        if (savedInstanceState == null) {
            setFragment(R.id.nav_dashboard);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onDestroy.forEach(Callbacks.VoidCallback::callback);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_dashboard, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else {
            setFragment(id);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isActiveFragment(ListFragment.class)) {
            ((ListFragment)activeFragment).fetchNewData();
        }
    }
}