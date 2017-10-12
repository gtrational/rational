package edu.gatech.cs2340.gtrational.rational.web;

import android.os.StrictMode;
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

import edu.gatech.cs2340.gtrational.rational.model.User;

/**
 * Created by Robert on 10/1/2017.
 */

public class WebAPI {

//    private static final String serverUrl = "http://10.0.2.2:8081";
    /**
     *  This URL gets changed to your local IP address if you're running it locally.
     *  On config.json, change the properties to match your local DB. Change host to be 0.0.0.0
     */
    private static final String serverUrl = "http://rational.tk:80";

    /**
     * A class to hold information about the login attempt
     */
    public static class LoginResult {
        public boolean success;
        public String errMsg;
        public String sessionID;
        public User.PermissionLevel permissionLevel;

        public LoginResult(boolean success, String err, String sess, int permissionLevelOrdinal) {
            this.success = success;
            this.errMsg = err;
            this.sessionID = sess;
            permissionLevel = User.PermissionLevel.values()[permissionLevelOrdinal];
        }
    }

    /**
     * A class to hold information about the registration attempt
     */
    public static class RegisterResult {
        public boolean success;
        public String errMsg;

        public RegisterResult(boolean success, String err) {
            this.success = success;
            this.errMsg = err;
        }
    }

    /**
     * A class to hold information about a rat sighting
     */
    public static class RatData {

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
     *
     * @return the server's response as a JSONObject.
     */
    private static JSONObject webRequest(String endpoint, JSONObject data) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
                return null;
            }

            try {
                return new JSONObject(resp);
            } catch (JSONException e) {
                Log.w("WebAPI", e);
                return null;
            }
        } catch (IOException e) {
            Log.w("WebAPI", e);
            return null;
        }
    }

    /**
     * Will attempt to login the user using our backend
     *
     * @param username The username
     * @param password The password
     * @return Information about the login attempt
     */
    public static LoginResult login(String username, String password) {
        try {
            JSONObject loginRequest = new JSONObject()
                    .put("username", username)
                    .put("password", password);
            JSONObject response = webRequest("/api/login", loginRequest);

            if (response == null) {
                return new LoginResult(false, "No server response", null, 0);
            } else if (response.has("err")) {
                return new LoginResult(false, response.getString("err"), null, 0);
            } else {
                return new LoginResult(true, null, response.getString("sessionID"), 0);
            }

        } catch (JSONException e) {
            Log.w("WebAPI", e);
            return new LoginResult(false, "Invalid username or password", null, 0);
        }
    }

    /**
     * Will attempt to register the user using our backend
     *
     * @param username The username
     * @param password The password
     * @return Information about the registration attempt
     */
    public static RegisterResult register(String username, String password, User.PermissionLevel permissionLevel) {
        try {
            JSONObject loginRequest = new JSONObject()
                    .put("username", username)
                    .put("password", password)
                    .put("permLevel", permissionLevel.ordinal());
            JSONObject response = webRequest("/api/register", loginRequest);

            if (response == null) {
                return new RegisterResult(false, "No server response");
            } else if (response.has("err")) {
                return new RegisterResult(false, response.getString("err"));
            } else {
                return new RegisterResult(true, null);
            }

        } catch (JSONException e) {
            Log.w("WebAPI", e);
            return new RegisterResult(true, "Invalid data entered");
        }
    }

    public static List<RatData> fetchPrelimRatData() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        JSONObject param = new JSONObject();
        JSONObject ratData = webRequest("/api/fetchPrelimRatData", param);
        try {
            JSONArray arr = ratData.getJSONArray("ratData");
            List<RatData> list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                list.add(new RatData(arr.getJSONObject(i)));
            }
            return list;
        } catch (JSONException e) {
            Log.w("WebAPI", e);
            return null;
        }
    }

}
