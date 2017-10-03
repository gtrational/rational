package edu.gatech.cs2340.gtrational.rational.web;

import edu.gatech.cs2340.gtrational.rational.model.User;

/**
 * Created by Robert on 10/1/2017.
 */

public class WebAPI {

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
     * Will attempt to login the user using our backend
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
     * @param username The username
     * @param password The password
     * @return Information about the registration attempt
     */
    public static RegisterResult register(String username, String password, User.PermissionLevel permissionLevel) {
        boolean success = FakeBackend.register(username, password, permissionLevel.ordinal());
        return new RegisterResult(success, success ? null : "Username taken");
    }

}
