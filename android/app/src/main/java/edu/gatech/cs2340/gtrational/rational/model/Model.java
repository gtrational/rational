package edu.gatech.cs2340.gtrational.rational.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import edu.gatech.cs2340.gtrational.rational.Callbacks;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

/**
 * Created by shyamal on 10/2/17.
 */

public class Model {
    /**
     * List of available topics
     */
    public static final String USER_UPDATE = "user.update";
    public static final String RAT_SIGHTING_UPDATE = "rat_sighting.update";

    private static Model instance = new Model();

    private User user;
    private List<WebAPI.RatData> ratSightings;
    private Map<Integer, WebAPI.RatData> ratDataMap;

    private void mapRatList(List<WebAPI.RatData> data) {
        for (WebAPI.RatData dat : data) {
            ratDataMap.put(dat.uniqueKey, dat);
        }
    }

    public User getUser() {
        return user;
    }

    /**
     * Map of listeners from topics to callbacks
     */
    private Map<String, List<ModelUpdateListener>> updateListeners;

    private Model() {
        ratSightings = Collections.synchronizedList(new ArrayList<>());
        ratDataMap = new HashMap<>();
        updateListeners = new HashMap<>();
    }

    /**
     * Static method to access singleton instance.
     * @return singleton instance
     */
    public static Model getInstance(){
        return instance;
    }

    /**
     * Register a new listener to the model on the given topic
     *
     * @param topic a string representing the topic of information being subscribed to
     * @param listener a callback to be passed information about the update which occurred
     */
    public Callbacks.VoidCallback registerListener(String topic, ModelUpdateListener listener) {
        if (updateListeners.containsKey(topic)) {
            updateListeners.get(topic).add(listener);
        } else {
            ArrayList<ModelUpdateListener> newListener = new ArrayList<>();
            newListener.add(listener);

            updateListeners.put(topic, newListener);
        }

        return () -> {
            updateListeners.get(topic).remove(listener);
        };
    }

    /**
     * Public info to all listeners for a certain topic about an update to data in the model.
     *
     * @param topic topic to publish to
     * @param updateInfo JSONObject containing info about what data in the model was updated.
     */
    public void publish(String topic, JSONObject updateInfo) {
        if (!updateListeners.containsKey(topic)) {
            return;
        }

        for (ModelUpdateListener listener : updateListeners.get(topic)) {
            listener.callback(updateInfo);
        }
    }

    /**
     * Format: {email, sessionID, permLevel}
     * @param userInfo
     */
    public void updateUser(JSONObject userInfo) {
        try {
            user = new User(userInfo.getString("email"), userInfo.getString("sessionID"), User.PermissionLevel.values()[userInfo.getInt("permLevel")]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        publish(USER_UPDATE, userInfo);
    }

    /**
     * Updates the list with the newest rat sightings
     * @param callback The callback to be executed once data is updated
     */
    public void getNewestRatData(Callbacks.AnyCallback<List<WebAPI.RatData>> callback) {
        if (ratSightings.isEmpty()) {
            return;
        }
        WebAPI.getRatSightingsAfter(ratSightings.get(0).uniqueKey, (List<WebAPI.RatData> ratList) -> {
            ratSightings.addAll(0, ratList);
            mapRatList(ratList);
            callback.callback(ratList);
        });
    }

    /**
     *
     * @param start The starting index of the desired block
     * @param size The size of the desired block
     * @param callback The function to be executed
     * @return The queried block
     */
    public void getRatData(int start, int size, Callbacks.AnyCallback<List<WebAPI.RatData>> callback) {
        if (start + size > ratSightings.size()) {
            int lastKey = 0;
            if (!ratSightings.isEmpty()) {
                lastKey = (ratSightings.get(ratSightings.size() - 1)).uniqueKey;
            }
            WebAPI.getRatSightings(lastKey, size, (List<WebAPI.RatData> list ) -> {
                Log.w("tag", "Model resp: " + list);
                ratSightings.addAll(list);
                mapRatList(list);
                ArrayList<WebAPI.RatData> query = new ArrayList<>();
                synchronized(ratSightings) {
                    for (int i = start; i < start + size; i++) {
                        query.add(ratSightings.get(i));
                    }
                }
                callback.callback(query);
            });
        } else {
            ArrayList<WebAPI.RatData> query = new ArrayList<>();
            synchronized(ratSightings) {
                for (int i = start; i < start + size; i++) {
                    query.add(ratSightings.get(i));
                }
            }
            callback.callback(query);
        }
    }

    /**
     *
     * @param startDate The start date for the search
     * @param endDate The end date of the search
     * @param callback The function to be exectuted on the data
     */
    public void getDateRangeRatsData(long startDate, long endDate, Callbacks.AnyCallback<List<WebAPI.RatData>> callback) {
        recursiveDateCallBack(startDate, ()-> {
            synchronized (ratSightings) {
                List<WebAPI.RatData> valid = new ArrayList<WebAPI.RatData>();
                for(int i = 0; i < ratSightings.size(); i++) {
                    if (ratSightings.get(i).createdTime >= startDate && ratSightings.get(i).createdTime <= endDate) {
                        valid.add(ratSightings.get(i));
                    }
                }
                callback.callback(valid);
            }
        });


    }

    /**
     *
     * @param startDate Continually asks for more data until we have data older than startDate
     * @param callback The function to be executed once all the data is populated
     */
    private void recursiveDateCallBack(long startDate, Callbacks.VoidCallback callback) {
        if (!ratSightings.isEmpty() && ratSightings.get(ratSightings.size() - 1).createdTime < startDate) {
            callback.callback();
            return;
        }
        getRatData(ratSightings.size(), 100, (List<WebAPI.RatData> list) -> {
            if (list == null || list.size() == 0) {
                callback.callback();
                return;
            } else {
                recursiveDateCallBack(startDate, callback);
            }
        });
    }

    /**
     * Finds and updates the rat in the model ratSightings if it exists in the list
     * @param updatedRat The rat that was updated
     */
    public void updateRatSighting(WebAPI.RatData updatedRat) {
        //TODO: update getters and setters once ratSighting class is complete
        int key = updatedRat.uniqueKey;
        synchronized(ratSightings) {
            int a = 0;
            int b = ratSightings.size();
            while(a < b) {
                int avg = (a + b)/2;
                if (ratSightings.get(avg).uniqueKey== key) {
                    ratSightings.set(avg, updatedRat);
                    publish(RAT_SIGHTING_UPDATE, updatedRat.toJson());
                    return;
                }
                if (ratSightings.get(avg).uniqueKey < key) {
                    a = avg + 1;
                } else {
                    b = avg;
                }
            }
        }
    }
    public WebAPI.RatData getRatDataByKey(int uniqueKey) {
        return ratDataMap.get(uniqueKey);
    }
}
