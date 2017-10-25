package edu.gatech.cs2340.gtrational.rational.controller;


import android.os.Bundle;
import android.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
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
        Model.getInstance().getRatData(0, 10, (List<WebAPI.RatData> rat_datas) -> {
            addPins(rat_datas);
        });

        return view;
    }

    public void addPins(List<WebAPI.RatData> rat_datas) {
        // first clear everything
        map.clear();
        for (int i = 0; i < rat_datas.size(); i++) {
            WebAPI.RatData data = rat_datas.get(i);
            map.addMarker(new MarkerOptions().position(new LatLng(data.latitude, data.longitude)).title(data.uniqueKey + ""));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.7143, -73.9376), 11));
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
}
