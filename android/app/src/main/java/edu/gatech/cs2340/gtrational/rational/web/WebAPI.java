package edu.gatech.cs2340.gtrational.rational.web;

/**
 * Created by Robert on 10/1/2017.
 */

public class WebAPI {

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

    public static class RegisterResult {
        public boolean success;
        public String errMsg;
        public RegisterResult(boolean success, String err) {
            this.success = success;
            this.errMsg = err;
        }
    }

    public static LoginResult login(String username, String password) {
        String session = FakeBackend.login(username, password);
        if (session == null) {
            return new LoginResult(false, "Invalid User", null);
        } else {
            return new LoginResult(true, null, session);
        }
    }

    public static RegisterResult register(String username, String password) {
        boolean success = FakeBackend.register(username, password);
        return new RegisterResult(success, success ? null : "Username taken");
    }
}
