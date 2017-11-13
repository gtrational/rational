package edu.gatech.cs2340.gtrational.rational.model.web;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.gtrational.rational.Callbacks;
import edu.gatech.cs2340.gtrational.rational.model.Model;
import edu.gatech.cs2340.gtrational.rational.model.RationalConfig;
import edu.gatech.cs2340.gtrational.rational.model.User;

/**
 * Created by Robert on 10/1/2017.
 * A class to handle calls to our backend api
 */

public final class WebAPI {

    private static final String serverUrl = RationalConfig.getSetting(RationalConfig.HOST_URL);
    private static final boolean printWebRequests = true;

    private static void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    /**
     * A class to hold information about the login attempt
     */
    public static class LoginResult {
        public final boolean success;
        public final String errMsg;
        public final String sessionID;
        public final User.PermissionLevel permissionLevel;

        public LoginResult(boolean success, String err, String session, int permissionLevelOrdinal) {
            this.success = success;
            this.errMsg = err;
            this.sessionID = session;
            permissionLevel = User.PermissionLevel.values()[permissionLevelOrdinal];
        }
    }

    /**
     * A class to hold information about the registration attempt
     */
    public static class RegisterResult {
        public final boolean success;
        public final String errMsg;

        public RegisterResult(boolean success, String err) {
            this.success = success;
            this.errMsg = err;
        }
    }

    /**
     * A class to hold information about a rat sighting
     */
    public static class RatData {

        public static class LatLon {
            public final double lat;
            public final double lon;

            public LatLon(double lat, double lon) {
                this.lat = lat;
                this.lon = lon;
            }
        }

        public static class AddressInfo {
            public final String address;
            public final String city;
            public final String borough;
            public final int zipCode;
            public final double lat;
            public final double lon;

            private AddressInfo(String address, String city, String borough, int zipCode, LatLon latLon) {
                this.address = address;
                this.city = city;
                this.borough = borough;
                this.zipCode = zipCode;
                this.lat = latLon.lat;
                this.lon = latLon.lon;
            }

            public static AddressInfo of(String address, String city, String borough, int zipCode, LatLon latLon) {
                return new AddressInfo(address, city, borough, zipCode, latLon);
            }
        }

        public int uniqueKey;
        public long createdTime;
        public String locationType;
        public int incidentZip;
        public String incidentAddress;
        public String city;
        public String borough;
        public double latitude;
        public double longitude;

        public RatData (JSONObject obj) throws JSONException {
            uniqueKey = obj.getInt("unique_key");
            createdTime = obj.getLong("created_date");
            locationType = obj.getString("location_type");
            incidentZip = obj.getInt("incident_zip");
            incidentAddress = obj.getString("incident_address");
            city = obj.getString("city");
            borough = obj.getString("borough");
            latitude = obj.getDouble("latitude");
            longitude = obj.getDouble("longitude");
        }

        public RatData (int uniqueKey, long createdTime, String locationType, AddressInfo addressInfo) {
            this.uniqueKey = uniqueKey;
            this.createdTime = createdTime;
            this.locationType = locationType;
            this.incidentZip = addressInfo.zipCode;
            this.incidentAddress = addressInfo.address;
            this.city = addressInfo.city;
            this.borough = addressInfo.borough;
            this.latitude = addressInfo.lat;
            this.longitude = addressInfo.lon;
        }

        public JSONObject toJson() {
            RatData rData = this;
            JSONObject rData_json = new JSONObject();
            try {
                rData_json.put("unique_key", rData.uniqueKey);
                rData_json.put("created_date", rData.createdTime);
                rData_json.put("locationType", rData.locationType);
                rData_json.put("incident_zip", rData.incidentZip);
                rData_json.put("incidentAddress", rData.incidentAddress);
                rData_json.put("city", rData.city);
                rData_json.put("borough", rData.borough);
                rData_json.put("latitude", rData.latitude);
                rData_json.put("longitude", rData.longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return rData_json;
        }
    }

    public static class RatDataResult {
        public final boolean success;
        final String error_message;

        RatDataResult(boolean is_success, String error) {
            success = is_success;
            error_message = error;
        }
    }

    /**
     * Reads all lines of an InputStream into a string and returns it.
     *
     * @param stream InputStream
     * @return String of all lines of InputStream
     */
    private static String readStream(InputStream stream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder str = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            return str.toString();

        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Makes a HTTP POST request to the server at the route specified route, and
     * returns with the server's response.
     *
     * @param endpoint the route to which to make the request on the server.
     * @param data a JSONObject containing the data to send to the server as part of the request.
     */
    private static void webRequest(String endpoint, JSONObject data, Callbacks.JSONExceptionCallback<JSONObject> callback) {
        if (printWebRequests) {
            try {
                Log.i("tag", "Making web request to " + endpoint + " with " + data.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        runAsync(() -> {
            String content = data.toString();
            try {
                URL url = new URL(serverUrl + endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set flags on request
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Content-Length", content.length() + "");

                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(content.getBytes());

                String resp = readStream(conn.getInputStream());

                if (resp == null) {
                    String err = readStream(conn.getErrorStream());
                    Log.w("WebAPI", "Http Error (code " + conn.getResponseCode() + "): " + err);
                    try {
                        callback.callback(null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
                    JSONObject respJson = new JSONObject(resp);
                    if (printWebRequests) {
                        Log.d("tag", "Got from " + endpoint + ": " + respJson.toString(2));
                    }
                    callback.callback(respJson);
                } catch (JSONException e) {
                    Log.w("tag", "Also got jexception " + e.toString());
                    try {
                        callback.callback(null);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (IOException e) {
                Log.w("WebAPI", e);
                try {
                    callback.callback(null);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * Will attempt to login the user using our backend
     *
     * @param email The username
     * @param password The password
     */
    public static void login(String email, String password, Callbacks.AnyCallback<LoginResult> callback) {
        try {
            JSONObject loginRequest = new JSONObject()
                    .put("email", email)
                    .put("password", password);
            webRequest("/api/login", loginRequest, (JSONObject response) -> {
                if (response == null) {
                    callback.callback(new LoginResult(false, "No server response", null, 0));
                } else if (response.has("err")) {
                    callback.callback(new LoginResult(false, response.getString("err"), null, 0));
                } else {
                    callback.callback(new LoginResult(true, null, response.getString("sessionid"), response.getInt("permLevel")));
                }
            });
        } catch (JSONException e) {
            Log.w("WebAPI", e);
            callback.callback(new LoginResult(false, "Invalid data entered", null, 0));
        }
    }

    /**
     * Will attempt to register the user using our backend
     *
     * @param email The username
     * @param password The password
     */
    public static void register(String email, String password, User.PermissionLevel permissionLevel, Callbacks.AnyCallback<RegisterResult> callback) {
        try {
            JSONObject loginRequest = new JSONObject()
                    .put("email", email)
                    .put("password", password)
                    .put("permLevel", permissionLevel.ordinal());
            webRequest("/api/register", loginRequest, (JSONObject response) -> {
                if (response == null) {
                    callback.callback(new RegisterResult(false, "No server response"));
                } else if (response.has("err")) {
                    callback.callback(new RegisterResult(false, response.getString("err")));
                } else {
                    callback.callback(new RegisterResult(true, null));
                }
            });
        } catch (JSONException e) {
            Log.w("WebAPI", e);
            callback.callback(new RegisterResult(true, "Invalid data entered"));
        }
    }

    public static void addRatSighting(RatData rData, Callbacks.AnyCallback<RatDataResult> callback) {
        JSONObject json = rData.toJson();
        try {
            json.put("sessionid", Model.getInstance().getUser().getSessionId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webRequest("/api/postRatSightings", json, (JSONObject object) -> callback.callback(new RatDataResult(true, null)));
    }

    public static void getRatSightingsAfter(int start_id, Callbacks.AnyCallback<List<RatData>> callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("sessionid", Model.getInstance().getUser().getSessionId());
            json.put("startid", start_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webRequest("/api/getRatSightingsAfter", json, (JSONObject results) -> {
            List<RatData> ratData = new ArrayList<>();
            JSONArray array_results = results.getJSONArray("ratData");
            for (int i = 0; i < array_results.length(); i++) {
                ratData.add(new RatData(array_results.getJSONObject(i)));
            }

            callback.callback(ratData);
        });
    }
    /**
     * A method to fetch RatData from the backend
     */
    public static void getRatSightings(int startId, int limit, Callbacks.AnyCallback<? super List<RatData>> callback) {
        List<RatData> ratData = new ArrayList<>();
        JSONObject request = new JSONObject();

        try {
            request.put("sessionid", Model.getInstance().getUser().getSessionId());
            request.put("startid", startId);
            request.put("limit", limit);
        } catch (JSONException ex) {
            Log.w("WebAPI", ex);
        }

        webRequest("/api/getRatSightings", request, (JSONObject results) -> {
            //TODO this is a temporary fix, we need to figure out why it returns null sometimes
            if (results == null) {
                return;
            }
            Log.d("tag", "Results: " + results.toString(2));
            // extract result, put the into callback
            JSONArray array_results = results.getJSONArray("ratData");
            for (int i = 0; i < limit; i++) {
                ratData.add(new RatData(array_results.getJSONObject(i)));
            }

            Log.d("tag", "Calling callback");
            callback.callback(ratData);
        });
    }
}
