package edu.gatech.cs2340.gtrational.rational.model.web;

import edu.gatech.cs2340.gtrational.rational.model.models.User;

/**
 * Created by daniel on 10/18/17.
 */

/**
 * Class to represent the result of a login attempt.
 */
public class LoginResult extends GenericResult {

    /**
     * New session ID of user if successful login
     */
    public String sessionId;

    /**
     * Permission Level of user if successful login
     */
    public User.PermissionLevel permissionLevel;

    public LoginResult(boolean success, String err, String sessionId, User.PermissionLevel permissionLevel) {
        super(success, err);

        this.sessionId = sessionId;
        this.permissionLevel = permissionLevel;
    }

}
