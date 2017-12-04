package edu.gatech.cs2340.gtrational.rational.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.model.Model;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

/**
 * A fragment for the "all rat sightings" screen.
 */
public class ListFragment extends Fragment {

    private static final int DEFAULT_RAT_NUM = 20;

    private List<HashMap<String, String>> listItems;
    private Map<Integer, HashMap<String, String>> listMap;

    private HashMap<String, String> buildRatData(WebAPI.RatData data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd KK:mm:ss aa", Locale.US);
        HashMap<String, String> item = new HashMap<>();
        item.put("line1", data.uniqueKey + "");
        item.put("line2", sdf.format(new Date(data.createdTime)) + "");
        item.put("line3", data.borough + ", " + data.city);
        return item;
    }

    /**
     * Updates the rat data
     * @param ratInfo The new data
     */
    public void onRatUpdate(WebAPI.RatData ratInfo) {
        new ExecuteTask(ratInfo).execute();
    }

    private void fetchOldData() {
        Log.w("tag", "fetchOldData");
        Model.getInstance().getRatData(listItems.size(), DEFAULT_RAT_NUM,
                (List<WebAPI.RatData> newData) -> {
            Log.w("tag", "olddat: " + newData);
            new ExecuteTask(newData, false).execute();
        });
    }

    /**
     * Will fetch new data
     */
    public void fetchNewData() {
        Log.w("tag", "fetchNewData");
        Model.getInstance().getNewestRatData((List<WebAPI.RatData> newData) -> {
            Log.w("ListFragment", "newdat: " + newData);
            new ExecuteTask(newData, true).execute();
            @SuppressWarnings("ConstantConditions") SwipeRefreshLayout swipeLayout = getView()
                    .findViewById(R.id.swipe_layout);
            swipeLayout.setRefreshing(false);
        });
    }

    /**
     * Creates the List Fragment View
     *
     * @param inflater           Layout Inflater object
     * @param container          Container object
     * @param savedInstanceState State object
     * @return returns View
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listItems = new ArrayList<>();
        listMap = new HashMap<>();

        ListView theList = view.findViewById(R.id.listview);
        ListAdapter sa = new SimpleAdapter(this.getActivity(), listItems, R.layout.row_layout,
                new String[]{"line1", "line2", "line3"},
                new int[]{R.id.line1, R.id.line2, R.id.line3});
        theList.setAdapter(sa);

        theList.setOnItemClickListener((AdapterView<?> adapterView, View view1, int i, long l) -> {
            Intent intent = new Intent(getActivity(), ViewDataActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("text",
                    (String) ((TextView) view1.findViewById(R.id.line1)).getText());
            intent.putExtras(bundle);
            startActivity(intent);
        });

        SwipeRefreshLayout swipeLayout = view.findViewById(R.id.swipe_layout);
        swipeLayout.setOnRefreshListener(this::fetchNewData);

        theList.setOnScrollListener(new GoGoScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onLoadMore() {
                fetchOldData();
            }
        });

        fetchOldData();

        return view;
    }

    private class ExecuteTask extends AsyncTask<Void, Void, Void> {

        private WebAPI.RatData dataToUpdate;
        private List<WebAPI.RatData> newData;
        private boolean isNew;

        public ExecuteTask(WebAPI.RatData dataToUpdate) {
            this.dataToUpdate = dataToUpdate;
        }

        public ExecuteTask(List<WebAPI.RatData> newData, boolean isNew) {
            this.newData = new ArrayList<>(newData);
            this.isNew = isNew;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dataToUpdate != null) {
                if (listMap.containsKey(dataToUpdate.uniqueKey)) {
                    Map<String, String> newObj = buildRatData(dataToUpdate);
                    Map<String, String> oldData = listMap.get(dataToUpdate.uniqueKey);
                    for (String key : newObj.keySet()) {
                        oldData.put(key, newObj.get(key));
                    }
                }
            }

            if (newData != null) {
                if (isNew) {
                    for (int i = newData.size() - 1; i >= 0; i--) {
                        listItems.add(0, buildRatData(newData.get(i)));
                    }
                } else {
                    for (int i = 0; i < newData.size(); i++) {
                        listItems.add(buildRatData(newData.get(i)));
                    }
                }
            }

            View view = getView();
            if (view != null) {
                ListView theList = getView().findViewById(R.id.listview);
                BaseAdapter adapter = (BaseAdapter)theList.getAdapter();
                adapter.notifyDataSetChanged();
            }
        }
    }
}
