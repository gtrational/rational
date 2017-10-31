package edu.gatech.cs2340.gtrational.rational.controller;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import edu.gatech.cs2340.gtrational.rational.R;

/**
 * A fragment for the "Nearby" screen.
 */
public class GraphFragment extends Fragment {


    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
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
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);

        GraphView graph = view.findViewById(R.id.graph);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, -1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 200)
        });
        graph.addSeries(series);


        // styling
//        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
//            @Override
//            public int get(DataPoint data) {
//                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
//            }
//        });
//
        series.setSpacing(50);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);

        // Inflate the layout for this fragment
        return view;
    }

}
