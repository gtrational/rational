package edu.gatech.cs2340.gtrational.rational.model;

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
    private List<RatSighting> ratSightings;

    public User getUser() {
        return user;
    }

    /**
     * Map of listeners from topics to callbacks
     */
    private Map<String, List<ModelUpdateListener>> updateListeners;

    private Model() {
        ratSightings = Collections.synchronizedList(new ArrayList<>());
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
     *
     * @param start The starting index of the desired block
     * @param size The size of the desired block
     * @return The queried block
     */
    public List<RatSighting> getRatData(int start, int size) {
        if (start + size > ratSightings.size()) {
            //int lastKey (ratSightings.get(ratSightings.size() - 1)).getKey()
            //getRatSightings(lastKey, start + size - ratSightings.size());
        }
        ArrayList<RatSighting> query = new ArrayList<>();
        synchronized(ratSightings) {
            for (int i = start; i < start + size; i++) {
                query.add(ratSightings.get(i));
            }
        }
        return query;
    }
    public void updateRatSighting(RatSighting updatedRat) {
        //TODO: update getters and setters once ratSighting class is complete
        //long createdTime = updatedRat.getDate(); get the date
        synchronized(ratSightings) {
            int a = 0;
            int b = ratSightings.size();
            while(a < b) {
                int avg = (a + b)/2;
                if (ratSightings.get(avg).getDate() == createdTime) {
                    ratSightings.set(avg, updatedRat);
                    publish(RAT_SIGHTING_UPDATE, updatedRat);
                    return;
                }
                if (ratSightings.get(avg).getDate() < createdTime) {
                    a = avg + 1;
                } else {
                    b = avg;
                }
            }
        }
    }
}
