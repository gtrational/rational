package edu.gatech.cs2340.gtrational.rational.model;

import org.json.JSONObject;

/**
 * Created by daniel on 10/17/17.
 */

@FunctionalInterface
public interface ModelUpdateListener {

    void callback(JSONObject updateInfo);

}
