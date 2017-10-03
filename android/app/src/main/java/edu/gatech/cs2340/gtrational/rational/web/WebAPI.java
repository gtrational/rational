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
        public LoginResult(boolean success, String err, String sess) {
            this.success = success;
            this.errMsg = err;
            this.sessionID = sess;
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
        String session = FakeBackend.login(username, password);
        if (session == null) {
            return new LoginResult(false, "Invalid User", null);
        } else {
            return new LoginResult(true, null, session);
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
