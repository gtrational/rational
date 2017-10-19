package edu.gatech.cs2340.gtrational.rational.controller;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

/**
 * A fragment for the "all rat sightings" screen.
 */
public class ListFragment extends Fragment {

    private List<HashMap<String, String>> listItems;

    public ListFragment() {
        // Required empty public constructor
    }

    private HashMap<String, String> buildRatData(WebAPI.RatData data) {
        HashMap<String, String> item = new HashMap<>();
        item.put("line1", data.uniqueKey + "");
        item.put("line2", new SimpleDateFormat("yyyy/MM/dd KK:mm:ss aa").format(new Date(data.createdTime)) + "");
        item.put("line3", data.borough + ", " + data.city);
        return item;
    }

    public void onRatUpdate(JSONObject updateInfo) {
        try {
            new ExecuteTask(updateInfo.getString("name")).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates the List Fragment View
     *
     * @param inflater           Layout Inflator object
     * @param container          Container object
     * @param savedInstanceState State object
     * @return returns View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        ListView theList = view.findViewById(R.id.listview);
        SimpleAdapter sa = new SimpleAdapter(this.getActivity(), listItems, R.layout.row_layout, new String[]{"line1", "line2", "line3"}, new int[]{R.id.line1, R.id.line2, R.id.line3});
        theList.setAdapter(sa);

        theList.setOnItemClickListener((AdapterView<?> adapterView, View view1, int i, long l) -> {
            Intent intent = new Intent(getActivity(), ViewDataActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("text", (String) ((TextView) view1.findViewById(R.id.line1)).getText());
            intent.putExtras(bundle);
            startActivity(intent);
        });

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new ExecuteTask("boiu").execute();
        }).start();

        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO: ACTUALLY MAKE REFRESH DO SOMETHING
            }
        });

        theList.setOnScrollListener(new GoGoScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                //TODO: Do some call to WebAPI
                return true;
            }
        });

        return view;
    }

    private class ExecuteTask extends AsyncTask<Void, Void, Void> {

        private String newData;

        public ExecuteTask(String newData) {
            this.newData = newData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void voidd) {
            HashMap<String, String> item = new HashMap<>();
            item.put("line1", newData);
            item.put("line2", newData);
            item.put("line3", newData);
            listItems.add(item);

            ListView theList = getView().findViewById(R.id.listview);
            BaseAdapter adapter = (BaseAdapter)theList.getAdapter();
            adapter.notifyDataSetChanged();
        }
    }
}
