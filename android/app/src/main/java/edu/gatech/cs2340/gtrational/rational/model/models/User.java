package edu.gatech.cs2340.gtrational.rational.model.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by shyamal on 10/2/17.
 */

public class User {

    public enum PermissionLevel {
        USER, ADMIN;

        @JsonValue
        public int getValue() {
            return ordinal();
        }
    }

    private String email;
    private PermissionLevel permissionLevel;
    private String sessionId;

    public User(String email, String sessionId, PermissionLevel permissionlvl) {
        this.email = email;
        this.permissionLevel = permissionlvl;
    }

    /**
     * Retrieves the User's email
     *
     * @return the email of the User.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the User's permission level.
     *
     * @return the permission level of the User.
     */
    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    /**
     * Retrieves the User's sessionId
     *
     * @return
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Set the email of a user
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the permission level of the user
     *
     * @param permissionLevel the new permission level
     */
    public void setPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    /**
     * Set the sessionId of the user
     *
     * @param sessionId the new sessionId
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
