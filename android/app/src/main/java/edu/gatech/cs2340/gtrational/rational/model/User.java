package edu.gatech.cs2340.gtrational.rational.model;

import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

/**
 * Created by shyamal on 10/2/17.
 */

public class User {
    public enum PermissionLevel {
        USER, ADMIN;
    }

        private String username;
        private String password;
        private PermissionLevel permissionLvl;
        private String sessionId;

    public User(String username, String password, PermissionLevel permissionlvl) {
        this.username = username;
        this.password = password;
        this.permissionLvl = permissionlvl;
    }

    public User(String username, String password) {
        this(username, password, PermissionLevel.USER);
    }

    /**
     * Retrieves the User's username
     * @return the username of the User.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the User's password
     * @param pwd the password to set the User's password to.
     */
    public void setPassword(String pwd) {
        password = pwd;
    }

    /**
     * Retrieves the User's permission level.
     * @return the permission level of the User.
     */
    public PermissionLevel getPermissionLvl() {
        return permissionLvl;
    }

    /**
     * Sets the User's permission level.
     * @param lvl the level to set the User's permission level to.
     */
    public void setPermissionLvl(PermissionLevel lvl) {
        permissionLvl = lvl;
    }


    /**
     * Sets the User's session ID.
     * @param s the session ID to set for the User.
     * @return whether the operation was successful.
     */
    public boolean setSessionId(String s) {
        if (sessionId == null) {
            sessionId = s;
            return true;
        }
        return false;
    }

    /**
     * Ends the User's session.
     */
    public void endSession() {
        sessionId = null;
    }

    /**
     * Logs a User into the system and sets a session ID.
     * @return whether the operation was successful.
     */
    public boolean login() {
        if (username == null || password == null) {
            return false;
        }
        WebAPI.LoginResult result = WebAPI.login(username, password);
        if (result.success) {
            sessionId = result.sessionID;
            permissionLvl = result.permissionLevel;
            return true;
        }
        return false;
    }
}
