package edu.gatech.cs2340.gtrational.rational.controller;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.web.DataCache;
import edu.gatech.cs2340.gtrational.rational.web.WebAPI;

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

        List<WebAPI.RatData> data = DataCache.fetchRatData();

//        String[][] testData = {
//                {"426313", "2017-10-10T13:24:25", "Flushing, Queens, New York"},
//                {"102932", "2017-10-10T13:14:25", "Upper East Side, Manhattan, New York"},
//                {"719238", "2017-10-10T12:57:15", "Hell's Kitchen, Manhattan, New York"},
//                {"192859", "2017-10-10T12:21:28", "Laguardia, Queens, New York"}
//        };

        ArrayList<HashMap<String, String>> newList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            HashMap<String, String> item = new HashMap<>();
            item.put("line1", data.get(i).uniqueKey + "");
            item.put("line2", new SimpleDateFormat("yyyy/MM/dd KK:mm:ss aa").format(new Date(data.get(i).createdTime)) + "");
            item.put("line3", data.get(i).borough + ", " + data.get(i).city) ;
            newList.add(item);
        }

        ListView theList = (ListView) view.findViewById(R.id.listview);
        SimpleAdapter sa = new SimpleAdapter(this.getActivity(), newList, R.layout.row_layout, new String[] {"line1", "line2", "line3"}, new int[] {R.id.line1, R.id.line2, R.id.line3});
        theList.setAdapter(sa);

        theList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), ViewDataActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("text", (String) ((TextView) view.findViewById(R.id.line1)).getText());
            intent.putExtras(bundle);
            startActivity(intent);
            }
        });

        return view;
    }

}
