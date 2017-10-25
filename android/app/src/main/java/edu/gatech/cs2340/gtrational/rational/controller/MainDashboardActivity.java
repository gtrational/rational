package edu.gatech.cs2340.gtrational.rational.controller;

import android.app.FragmentManager;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.cs2340.gtrational.rational.Callbacks;
import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.model.Model;
import edu.gatech.cs2340.gtrational.rational.model.ModelUpdateListener;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

public class MainDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static class FragInfo {
        public Class<? extends Fragment> fragmentClass;
        public String title;

        public FragInfo(Class<? extends Fragment> fragmentClass, String title) {
            this.fragmentClass = fragmentClass;
            this.title = title;
        }
    }

    private Map<Integer, FragInfo> fragments;
    private Fragment activeFragment;
    private List<Callbacks.VoidCallback> onDestroy;

    public MainDashboardActivity() {
        //Init Fragments
        fragments = new HashMap<>();
        fragments.put(R.id.nav_dashboard, new FragInfo(DashboardFragment.class, "Dashboard"));
        fragments.put(R.id.nav_nearby, new FragInfo(NearbyFragment.class, "Nearby"));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Subscribe to update
        onDestroy.add(Model.getInstance().registerListener(Model.RAT_SIGHTING_UPDATE, (JSONObject updateInfo) -> {
            if (activeFragment instanceof ListFragment) {
                try {
                    ((ListFragment) activeFragment).onRatUpdate(new WebAPI.RatData(updateInfo));
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
            startActivityForResult(intent, 999);
        });

        // Generate navigation drawer.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        for (Callbacks.VoidCallback cb : onDestroy) {
            cb.callback();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
        if (activeFragment instanceof ListFragment) {
            ((ListFragment)activeFragment).fetchNewData();
        }
    }
}