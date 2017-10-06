package edu.gatech.cs2340.gtrational.rational.web;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.gatech.cs2340.gtrational.rational.model.User;

/**
 * Created by Robert on 10/1/2017.
 */

public class WebAPI {

    private static final String serverUrl = "http://10.0.2.2:987/";

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

    private static JSONObject webRequest(String endpoint, JSONObject data) {
        String content = data.toString();
        try {
            URL url = new URL(serverUrl + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", content.length() + "");
            OutputStream os = conn.getOutputStream();
            os.write(content.getBytes());

            String resp = readStream(conn.getInputStream());

            if (resp == null) {
                String err = readStream(conn.getErrorStream());
                Log.d("Tag", "Http Error (code " + conn.getResponseCode() + "): " + err);
                return null;
            }

            try {
                return new JSONObject(resp);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        FakeBackend.LoginResponse response = FakeBackend.login(username, password);
        if (response == null) {
            return new LoginResult(false, "Invalid User", null, 0);
        } else {
            return new LoginResult(true, null, response.sessionID, response.permissionLevelOrdinal);
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
        boolean success = FakeBackend.register(username, password, permissionLevel.ordinal());
        return new RegisterResult(success, success ? null : "Username taken");
    }

}
