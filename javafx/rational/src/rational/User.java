/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rational;

/**
 *
 * @author james
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
