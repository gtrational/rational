package edu.gatech.cs2340.gtrational.rational.controller;

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


    public String getUsername() {
        return username;
    }

    public void setPassword(String pwd) {
        password = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String setEmail(String email) {
        this.email = email;
    }

    public void setPermissionLvl(PermissionLevel lvl) {
        permissionLvl = lvl;
    }

    public PermissionLevel getPermissionLvl() {
        return permissionLvl;
    }


    public boolean getIsLocked() {
        return isLocked;
    }


    public void lockUser() {
        isLocked = true;
    }

    public void unlockUser() {
        isLocked = false;
    }

    public boolean setSessionId(String s) {
        if (sessionId == null) {
            sessionId = s;
            return true;
        }
        return false;
    }

    public void endSession() {
        sessionId = null;
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        if (username.equals(this.username) && password.equals(this.password) && !isLocked) {
            //log in
            //sessionId = database.getSessionID();
            return true;
        }
        return false;
    }
}
