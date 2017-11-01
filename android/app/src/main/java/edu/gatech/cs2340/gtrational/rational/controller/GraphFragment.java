package edu.gatech.cs2340.gtrational.rational.controller;


import android.app.IntentService;
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
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

/**
 * A fragment for the "Nearby" screen.
 */
public class GraphFragment extends Fragment {

    private static final Calendar calendar = Calendar.getInstance();

    GraphView graph;
    BarGraphSeries<DataPoint> series;

    public GraphFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_graph, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_filter:
                GraphFilterDialogFragment filterDialog = new GraphFilterDialogFragment();
                filterDialog.show(getFragmentManager(), "Filter");
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        graph = view.findViewById(R.id.graph);
        graph.setTitle("Rat Sighting History");

        Calendar c = Calendar.getInstance();
        c.set(1990, 0, 1);
        Date d1 = c.getTime();
        c.set(1991, 0, 1);
        Date d2 = c.getTime();


        series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 10),
                new DataPoint(d2, 20)
        });
        graph.addSeries(series);

        series.setSpacing(50);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), new SimpleDateFormat("yy", Locale.US)));
        graph.getGridLabelRenderer().setNumHorizontalLabels(2);

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d2.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setMinY(0);
        graph.getViewport().setYAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

        // Inflate the layout for this fragment
        return view;
    }

    public void setGraphData(List<WebAPI.RatData> ratData, long start, long end, boolean byYear) {

        // set manual x bounds to have nice steps
        //graph.getViewport().setMinX(d1.getTime());
        //graph.getViewport().setMaxX(d2.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        // set date label formatter
        if (byYear) {
            int startYear = getYearFromTime(start);

            int endYear = getYearFromTime(end);

            int[] buckets = new int[startYear - endYear];
            for (WebAPI.RatData rat : ratData) {
                buckets[getYearFromTime(rat.createdTime) - startYear]++;
            }

            DataPoint[] newData = new DataPoint[buckets.length];
            for (int i = 0; i < buckets.length; i++) {
                calendar.set(startYear + i, 0, 1);
                newData[i] = new DataPoint(calendar.getTime(), buckets[i]);
            }

            series.resetData(newData);

            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), new SimpleDateFormat("yy", Locale.US)));
        } else {
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), new SimpleDateFormat("MM-yy", Locale.US)));
        }
        graph.getGridLabelRenderer().setNumHorizontalLabels(0);

    }

    private static int getYearFromTime(long time) {
        return Integer.parseInt(new SimpleDateFormat("yyyy", Locale.US).format(new Date(time)));
    }

}
