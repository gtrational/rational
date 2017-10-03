package edu.gatech.cs2340.gtrational.rational.model;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.gtrational.rational.web.FakeBackend;
import edu.gatech.cs2340.gtrational.rational.web.WebAPI;

/**
 * Created by shyamal on 10/2/17.
 */

public class User {
    public enum PermissionLevel {
        USER, ADMIN, GOVERNMENT_OFFICIAL;
    }

    private String username;
    private String password;
    private String name;
    private String email;
    private PermissionLevel permissionLvl;
    private int phoneNumber;
    private boolean isLocked;
    private String sessionId;

    public User(String username, String password, String name, String email, PermissionLevel permissionlvl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.permissionLvl = permissionlvl;
    }

    public User(String username, String password, String name, String email) {
        this(username, password, name, email, PermissionLevel.USER);
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
     * Retrieves the User's name
     * @return the name of the User.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the User's name
     * @param name the name to set the User's name to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the User's email.
     * @return the email address of the User.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the User's email.
     * @param email the email to set the User's email to.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the User's phone number.
     * @return the User's phone number.
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the User's phone number.
     * @param phoneNumber the phone number to set the User's phone number to.
     */
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
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
     * Retrieves the User's locked status.
     * @return whether the user is locked.
     */
    public boolean getIsLocked() {
        return isLocked;
    }

    /**
     * Locks the user, regardless of current status.
     */
    public void lockUser() {
        isLocked = true;
    }

    /**
     * Unlocks the user, regardless of current status.
     */
    public void unlockUser() {
        isLocked = false;
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
     * @param username the username of the User to login.
     * @param password the password of the User to login.
     * @return whether the operation was successful.
     */
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        if (username.equals(this.username) && password.equals(this.password) && !isLocked) {
            this.sessionId = (WebAPI.login(username, password)).sessionID;
            return true;
        }
        return false;
    }
}
