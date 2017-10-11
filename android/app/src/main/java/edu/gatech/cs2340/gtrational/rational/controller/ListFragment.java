package edu.gatech.cs2340.gtrational.rational.controller;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.gatech.cs2340.gtrational.rational.R;

/**
 * A fragment for the "all rat sightings" screen.
 */
public class ListFragment extends Fragment {


    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list, container, false);

        String[][] testData = {
                {"LLKSDKJLJLKSDF", "!@KE@!#!@$!@", "()*SUDJLFKLKJSDF"},
                {"LLKSDKJLJLKSDF", "!@KE@!#!@$!@", "()*SUDJLFKLKJSDF"},
                {"LLKSDKJLJLKSDF", "!@KE@!#!@$!@", "()*SUDJLFKLKJSDF"},
                {"LLKSDKJLJLKSDF", "!@KE@!#!@$!@", "()*SUDJLFKLKJSDF"}
        };

        ArrayList<HashMap<String, String>> newList = new ArrayList<>();

        for (int i = 0; i < testData.length; i++) {
            HashMap<String, String> item = new HashMap<>();
            item.put("line1", testData[i][0]);
            item.put("line2", testData[i][1]);
            item.put("line3", testData[i][2]);
            newList.add(item);
        }

        ListView theList = (ListView) view.findViewById(R.id.listview);
        SimpleAdapter sa = new SimpleAdapter(this.getActivity(), newList, R.layout.row_layout, new String[] {"line1", "line2", "line3"}, new int[] {R.id.line1, R.id.line2, R.id.line3});
        theList.setAdapter(sa);
        return view;
    }

}
