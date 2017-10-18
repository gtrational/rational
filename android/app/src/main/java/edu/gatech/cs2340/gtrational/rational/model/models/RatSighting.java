package edu.gatech.cs2340.gtrational.rational.model.models;

/**
 * Created by daniel on 10/17/17.
 */


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Dummy class for RatSightings
 */
public class RatSighting {

    private int uniqueKey;

    private long createdTime;

    private String locationType;
    private int incidentZip;
    private String incidentAddress;
    private String city;
    private String borough;

    private double latitude;
    private double longitude;

//    public RatSighting (JSONObject obj) throws JSONException {
//        uniqueKey = obj.getInt("unique_key");
//        createdTime = obj.getLong("created_date");
//
//        locationType = obj.getString("location_type");
//        incidentZip = obj.getInt("incident_zip");
//        incidentAddress = obj.getString("incident_address");
//        city = obj.getString("city");
//        borough = obj.getString("borough");
//
//        latitude = obj.getDouble("latitude");
//        longitude = obj.getDouble("longitude");
//    }

    public RatSighting (int uniqueKey, long createdTime, String locationType, int incidentZip, String incidentAddress, String city, String borough, double latitude, double longitude) {
        this.uniqueKey = uniqueKey;
        this.createdTime = createdTime;
        this.locationType = locationType;
        this.incidentZip = incidentZip;
        this.incidentAddress = incidentAddress;
        this.city = city;
        this.borough = borough;
        this.latitude = latitude;
        this.longitude = longitude;
    }

//    public JSONObject toJson() {
//        RatData rData = this;
//        JSONObject rData_json = new JSONObject();
//        try {
//            rData_json.put("unique_key", rData.uniqueKey);
//            rData_json.put("created_date", rData.createdTime);
//            rData_json.put("locationType", rData.locationType);
//            rData_json.put("incident_zip", rData.incidentZip);
//            rData_json.put("incidentAddress", rData.incidentAddress);
//            rData_json.put("city", rData.city);
//            rData_json.put("borough", rData.borough);
//            rData_json.put("latitude", rData.latitude);
//            rData_json.put("longitude", rData.longitude);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return rData_json;
//    }

    public int getUniqueKey() {
        return uniqueKey;
    }

}
