package edu.gatech.cs2340.gtrational.rational;

import org.json.JSONException;

/**
 * Created by Robert on 10/16/2017.
 * A class that contains a bunch of callbacks
 */

public class Callbacks {

    @FunctionalInterface
    public interface VoidCallback {
        void callback();
    }

    @FunctionalInterface
    public interface AnyCallback<T> {
        void callback(T object);
    }

    @FunctionalInterface
    public interface JSONExceptionCallback<T> {
        void callback(T object) throws JSONException;
    }
}
