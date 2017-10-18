package edu.gatech.cs2340.gtrational.rational.model.web;

import android.util.Log;

import org.json.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import edu.gatech.cs2340.gtrational.rational.RationalApp;
import edu.gatech.cs2340.gtrational.rational.model.Model;
import edu.gatech.cs2340.gtrational.rational.model.RationalConfig;
import edu.gatech.cs2340.gtrational.rational.model.models.RatSighting;
import edu.gatech.cs2340.gtrational.rational.model.models.User;

/**
 * Created by Robert on 10/1/2017.
 */

public class WebAPI {

    private static final boolean printWebRequests = true;
    private static final String SERVER_URL = RationalApp.getInstance().getSetting(RationalConfig.HOSTURL);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static void runAsync(Runnable runnable) {
        new Thread(runnable).start();
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
     * @param content a JSON string containing the data to send to the server as part of the request.
     * @param callback a callback to handle some String
     */
    private static void webRequest(String endpoint, String content, Callbacks.IOExceptionCallback<String> callback) {
        if (printWebRequests) {
            Log.d("tag", "Making web request to " + endpoint + " with " + content);
        }
        runAsync(() -> {
            try {
                URL url = new URL(SERVER_URL + endpoint);
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
                    callback.callback(null);
                    return;
                }
                if (printWebRequests) {
                    Log.w("tag", "Got from " + endpoint + ": " + resp);
                }

                callback.callback(resp);

            } catch (IOException e) {
                Log.w("WebAPI", e);
                try {
                    callback.callback(null);
                } catch (IOException f) {
                    throw new RuntimeException("This should never happen...", f);
                }
            }
        });
    }

    /**
     * Will attempt to login the user using our backend
     *
     * @param email The username
     * @param password The password
     * @param callback A callback to be invoked asynchronously with the result
     */
    public static void login(String email, String password, Callbacks.AnyCallback<LoginResult> callback) {
        try {
            JSONObject loginRequest = new JSONObject()
                    .put("email", email)
                    .put("password", password);

            webRequest("/api/login", loginRequest, (String response) -> {
                if (response == null) {
                    callback.callback(new LoginResult(false, "No server response", null, User.PermissionLevel.USER));

                } else {
                    callback.callback(objectMapper.readValue(response, LoginResult.class));

                }
            });
        } catch (JSONException e) {
            Log.w("WebAPI", e);
            callback.callback(new LoginResult(false, "Invalid data entered", null, User.PermissionLevel.USER));
        }
    }

    /**
     * Will attempt to register the user using our backend
     *
     * @param email The username
     * @param password The password
     * @return Information about the registration attempt
     */
    public static void register(String email, String password, User.PermissionLevel permissionLevel, Callbacks.AnyCallback<GenericResult> callback) {
        try {
            JSONObject loginRequest = new JSONObject()
                    .put("email", email)
                    .put("password", password)
                    .put("permLevel", permissionLevel.ordinal());

            webRequest("/api/register", loginRequest, (String response) -> {
                if (response == null) {
                    callback.callback(new GenericResult(false, "No server response"));

                } else {
                    callback.callback(objectMapper.readValue(response, GenericResult.class));

                }
            });
        } catch (JSONException e) {
            Log.w("WebAPI", e);
            callback.callback(new GenericResult(true, "Invalid data entered"));
        }
    }

    public static void addRatSighting(RatSighting rData, Callbacks.AnyCallback<GenericResult> callback) {
        JSONObject json = new JSONObject()

        JSONObject json = rData.toJson();

        try {
            json.put("sessionid", Model.getInstance().getUser().getSessionId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webRequest("/api/postRatSightings", json, (String response) -> {
            if (response == null) {
                callback.callback(new GenericResult(false, "No server response"));
            } else {
                callback.callback(objectMapper.readValue(response, GenericResult.class));
            }
        });
    }

    public static void getRatSightingsAfter(int startid, Callbacks.AnyCallback<List<RatSighting>> callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("sessionid", Model.getInstance().getUser().getSessionId());
            json.put("startid", startid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webRequest("/api/getRatSightingsAfter", json, (JSONObject results) -> {
            List<RatSighting> ratData = new ArrayList<RatSighting>();
            JSONArray array_results = results.getJSONArray("ratData");
            for (int i = 0; i < array_results.length(); i++) {
                ratData.add(new RatSighting(array_results.getJSONObject(i)));
            }

            callback.callback(ratData);
        });
    }
    /**
     * A method to fetch RatData from the backend
     *
     * @return a List of RatData
     */
    public static void getRatSightings(int startId, int limit, Callbacks.AnyCallback<List<RatSighting>> callback) {
        List<RatSighting> ratData = new ArrayList<RatSighting>();
        JSONObject request = new JSONObject();

        System.out.println("Sessionid: " + Model.getInstance().getUser().getSessionId());

        try {
            request.put("sessionid", Model.getInstance().getUser().getSessionId());
            request.put("startid", startId);
            request.put("limit", limit);
        } catch (JSONException ex) {
            Log.w("WebAPI", ex);
        }

        webRequest("/api/getRatSightings", request, (String results) -> {
            //TODO this is a temporary fix, we need to figure out why it returns null sometimes
            if (results == null) {
                return;
            }
            Log.w("tag", "Results: " + results);
            // extract result, put the into callback
            JSONArray array_results = results.getJSONArray("ratData");
            for (int i = 0; i < limit; i++) {
                ratData.add(new RatData(array_results.getJSONObject(i)));
            }

            Log.w("tag", "Calling callback");
            callback.callback(ratData);
        });
    }
}
