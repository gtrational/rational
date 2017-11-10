package edu.gatech.cs2340.gtrational.rational.model;

/**
 * Created by Shyamal on 10/2/17.
 */

public class User {
    public enum PermissionLevel {
        USER, ADMIN
    }

    private String email;
    private PermissionLevel permissionLvl;
    private String sessionId;

    public User(String email, String sessionId, PermissionLevel permissionLevel) {
        this.email = email;
        this.sessionId = sessionId;
        this.permissionLvl = permissionLevel;
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
    public PermissionLevel getPermissionLvl() {
        return permissionLvl;
    }

    /**
     * Retrieves the User's sessionId
     *
     * @return
     */
    public String getSessionId() {
        return sessionId;
    }
}
