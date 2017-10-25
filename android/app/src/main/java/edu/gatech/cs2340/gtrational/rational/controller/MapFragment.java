package edu.gatech.cs2340.gtrational.rational.controller;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.gtrational.rational.Callbacks;
import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.model.Model;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

import static android.R.attr.data;
import static android.os.Build.VERSION_CODES.M;

/**
 * A fragment for the "Map" screen
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap map;
    MapView mapView;


    public MapFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_filter:
                MapFilterDialogFragment filterDialog = new MapFilterDialogFragment();
                filterDialog.show(getFragmentManager(), "Filter");
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // call add-pins method with list of 20 recent rat sightings.

        return view;
    }

    public void addPins(List<WebAPI.RatData> rat_datas) {
        // first clear everything
        new ExecuteTask(rat_datas).execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.7143, -73.9376), 11));
        Model.getInstance().getRatData(0, 10, (List<WebAPI.RatData> rat_datas) -> {
            addPins(rat_datas);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private class ExecuteTask extends AsyncTask<Void, Void, Void> {

        private List<WebAPI.RatData> rat_datas;

        public ExecuteTask(List<WebAPI.RatData> rat_datas) {
            this.rat_datas = rat_datas;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void voidd) {
            map.clear();
            for (int i = 0; i < rat_datas.size(); i++) {
                WebAPI.RatData data = rat_datas.get(i);
                map.addMarker(new MarkerOptions().position(new LatLng(data.latitude, data.longitude)).title(data.uniqueKey + ""));
            }
        }
    }
}
