package edu.gatech.cs2340.gtrational.rational.model;

/**
 * Created by Shyamal on 10/2/17.
 * A class to hold information about the user
 */

public class User {
    public enum PermissionLevel {
        USER, ADMIN
    }

    private final String email;
    private final PermissionLevel permissionLvl;
    private final String sessionId;

    /**
     * Constructor for user
     * @param email The email
     * @param sessionId The session id
     * @param permissionLevel The permission level
     */
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
     * @return The session id
     */
    public String getSessionId() {
        return sessionId;
    }
}
