package edu.gatech.cs2340.gtrational.rational.controller;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
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

//        calendar.set(2016, 0, 1);
//        Date d1 = calendar.getTime();
//        calendar.set(2017, 0, 1);
//        Date d2 = calendar.getTime();
//        calendar.set(2018, 0, 1);
//        Date d3 = calendar.getTime();
//        calendar.set(2019, 0, 1);
//        Date d4 = calendar.getTime();


        series = new BarGraphSeries<>(new DataPoint[] {});
        graph.addSeries(series);

        series.setSpacing(50);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);

//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), new SimpleDateFormat("yy", Locale.US)));
//        graph.getGridLabelRenderer().setNumHorizontalLabels(4);

        // set manual x bounds to have nice steps
//        graph.getViewport().setMinX(d1.getTime());
//        graph.getViewport().setMaxX(d4.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setMinY(0);
        graph.getViewport().setYAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

        // Inflate the layout for this fragment
        return view;
    }

    private void setGraphDataByYear(List<WebAPI.RatData> ratData, long start, long end) {
        int startYear = getYearFromTime(start);

        int endYear = getYearFromTime(end);

        int[] buckets = new int[endYear - startYear + 1];
        for (WebAPI.RatData rat : ratData) {
            buckets[getYearFromTime(rat.createdTime) - startYear]++;
        }

        DataPoint[] newData = new DataPoint[buckets.length];
        for (int i = 0; i < buckets.length; i++) {
            calendar.set(startYear + i, 0, 1);
            Log.w("Graphing", "" + (startYear + i));

            Date d = calendar.getTime();

            newData[i] = new DataPoint(d, buckets[i]);
        }

        Log.w("Graphing", Arrays.toString(newData));
        series.resetData(newData);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), new SimpleDateFormat("yy", Locale.US)));
        graph.getGridLabelRenderer().setNumHorizontalLabels(buckets.length);

        // set manual x bounds to have nice steps
        calendar.set(startYear, 0, 1);
        graph.getViewport().setMinX(calendar.getTime().getTime());

        calendar.set(endYear, 0, 1);
        graph.getViewport().setMaxX(calendar.getTime().getTime());


        Log.w("Graphing", startYear + " " + endYear);
    }

    private void setGraphDataNotByYear(List<WebAPI.RatData> ratData, long start, long end) {
        int startYear = getYearFromTime(start);
        int startMonth = getMonthFrontTime(start);

        int endYear = getYearFromTime(end);
        int endMonth = getMonthFrontTime(end);

        int[] buckets = new int[endMonth - startMonth + 12 * (endYear - startYear)];
        for (WebAPI.RatData rat : ratData) {
            int ratMonth = getMonthFrontTime(rat.createdTime);
            int ratYear = getYearFromTime(rat.createdTime);
            buckets[ratMonth - startMonth + 12 * (ratYear - startYear)]++;
        }

        DataPoint[] newData = new DataPoint[buckets.length];
        for (int i = 0; i < buckets.length; i++) {
            int month = (startMonth + i) % 12;
            int year = (startMonth + i) / 12;
            calendar.set(startYear + year, month, 1);
            Log.w("Graphing", "" + (startYear + year) + "-" + month);

            Date d = calendar.getTime();

            newData[i] = new DataPoint(d, buckets[i]);
        }

        Log.w("Graphing", Arrays.toString(newData));
        series.resetData(newData);


        // set manual x bounds to have nice steps
        calendar.set(startYear, startMonth, 1);
        graph.getViewport().setMinX(calendar.getTime().getTime());

        calendar.set(endYear, endMonth, 1);
        graph.getViewport().setMaxX(calendar.getTime().getTime());

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), new SimpleDateFormat("MM/yy", Locale.US)));
        graph.getGridLabelRenderer().setNumHorizontalLabels(buckets.length);

        Log.w("Graphing", startYear + "-" + startMonth + " " + endYear + "-" + endMonth);
    }

    /**
     * Changes the data in the graph based on the provided rat sightings, and the date range.
     *
     * @param ratData Rat data to use
     * @param start Start date to graph
     * @param end End date to graph
     * @param byYear Whether to graph by year or by month
     */
    public void setGraphData(List<WebAPI.RatData> ratData, long start, long end, boolean byYear) {
        if (byYear) {
            setGraphDataByYear(ratData, start, end);
        } else {
            setGraphDataNotByYear(ratData, start, end);
        }

        Log.w("Graphing", "Got here");
    }

    /**
     * Get the year as an integer from a millisecond time from epoch
     * @param time from epoch
     * @return year (e.g. 1998)
     */
    private static int getYearFromTime(long time) {
        return Integer.parseInt(new SimpleDateFormat("yyyy", Locale.US).format(new Date(time)));
    }

    /**
     * Get the month as an integer from a millisecond time from epoch.
     * @param time from epoch
     * @return month (e.g. January = 0)
     */
    private static int getMonthFrontTime(long time) {
        return Integer.parseInt(new SimpleDateFormat("MM", Locale.US).format(new Date(time))) - 1;
    }

}
