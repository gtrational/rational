package edu.gatech.cs2340.gtrational.rational.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Robert on 10/1/2017.
 * Fake Backend class to simulate our actual web backend that will be implemented later
 */

public class FakeBackend {

    /**
     * Random random random
     */
    private static final Random random = new Random();

    /**
     * FakeUser class that will hold information about a user
     */
    private static class FakeUser {

        /**
         * Enum to represent permission of this user
         */
        public enum PermissionLevel {
            USER,
            ADMIN;
        }

        /**
         * The username
         */
        public String username;

        /**
         * The password encrypted with plain-text encoding
         */
        public String password;

        /**
         * A list of this user's active web sessions
         */
        public List<String> sessions;

        /**
         * The permission level of this user (user / admin)
         */
        public PermissionLevel permissionLevel;

        public FakeUser(String username, String password, List<String> sessions, PermissionLevel permissionLevel) {
            this.username = username;
            this.password = password;
            this.sessions = sessions;
            this.permissionLevel = permissionLevel;
        }
    }

    /**
     * A list to keep track of all registered users
     */
    private static List<FakeUser> users = Collections.synchronizedList(new ArrayList<FakeUser>());

    /**
     * Will return the FakeUser instance corresponding to the provided username, or null if no user could be found
     * @param username The username
     * @return The FakeUser instance
     */
    private static FakeUser getByUsername(String username) {
        synchronized (users) {
            for (FakeUser user : users) {
                if (user.username.equals(username)) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Will return the FakeUser instance corresponding to the provided sessionID, or null if no user could be found
     * @param sessionID The sessionID
     * @return The FakeUser instance
     */
    private static FakeUser getBySessionID(String sessionID) {
        synchronized (users) {
            for (FakeUser user : users) {
                if (user.sessions.indexOf(sessionID) >= 0) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Will generate a new unique sessionID
     * @return The newly created sessionID
     */
    private static String newSessionID() {
        String alpha = "abcdefghijklmnopqrstuvwxyz123456790ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        String sessionID;
        do {
            sessionID = "";
            for (int i = 0; i < 10; i++) {
                sessionID += alpha.charAt(random.nextInt(alpha.length()));
            }
        } while (getBySessionID(sessionID) != null);

        return sessionID;
    }

    /**
     * Will "login" a user, by adding a sessionID to their FakeUser instance
     * @param username The username
     * @param password The password
     * @return The newly created sessionID
     */
    public static String login(String username, String password) {
        //TODO Add session to user if exists and return session, else return null
        return null;
    }

    /**
     * Will "logout" a user by removing the provided sessionID from their FakeUser instance
     * @param sessionID The user's sessionID
     */
    public static void logout(String sessionID) {
        //TODO Clear session from user if exists
    }

    /**
     * Will register the user by adding them to the list of users
     * @param username The username
     * @param password The password
     * @return True if user added successfully, or false if the username is taken
     */
    public static boolean register(String username, String password) {
        //TODO Add user
        return false;
    }
}
