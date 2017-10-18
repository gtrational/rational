package edu.gatech.cs2340.gtrational.rational.model.web;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by daniel on 10/18/17.
 */

/**
 * Generic result to a webRequest containing and indicator of success, and and error if necessary
 */
public class GenericResult {

    /**
     * Whether the request was successful or not
     */
    public boolean success;

    /**
     * An error message if the request failed
     */
    public String err;

    public GenericResult(boolean success, String err) {
        this.success = success;
        this.err = err;
    }


}
