/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rational;

import org.json.JSONException;
/**
 *
 * @author james
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
