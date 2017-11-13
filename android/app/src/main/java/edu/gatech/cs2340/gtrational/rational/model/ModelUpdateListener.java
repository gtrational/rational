package edu.gatech.cs2340.gtrational.rational.model;

import org.json.JSONObject;

/**
 * Created by daniel on 10/17/17.
 * Another listener
 */

@FunctionalInterface
public interface ModelUpdateListener {

    /**
     * The void callback
     * @param updateInfo The callback json data
     */
    void callback(JSONObject updateInfo);

}
