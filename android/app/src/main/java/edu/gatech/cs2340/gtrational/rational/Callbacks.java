package edu.gatech.cs2340.gtrational.rational;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Robert on 10/16/2017.
 */

public class Callbacks {

    public interface VoidCallback {
        void callback();
    }

    public interface AnyCallback<T> {
        void callback(T object);
    }

    public interface JSONExceptionCallback<T> {
        void callback(T object) throws JSONException;
    }
}
