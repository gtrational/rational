package edu.gatech.cs2340.gtrational.rational;

import org.json.JSONException;

/**
 * Created by Robert on 10/16/2017.
 * A class that contains a bunch of callbacks
 */

public class Callbacks {

    @FunctionalInterface
    public interface VoidCallback {
        /**
         * A void callback
         */
        void callback();
    }

    @FunctionalInterface
    public interface AnyCallback<T> {
        /**
         * A void callback of type object
         * @param object The type of callback
         */
        void callback(T object);
    }

    @FunctionalInterface
    public interface JSONExceptionCallback<T> {
        /**
         * A void callback with type of object that can throw JSONException
         * @param object The type of object
         * @throws JSONException The exception that it throws
         */
        void callback(T object) throws JSONException;
    }
}
