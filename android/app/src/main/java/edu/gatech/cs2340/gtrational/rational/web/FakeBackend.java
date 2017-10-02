package edu.gatech.cs2340.gtrational.rational.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Robert on 10/1/2017.
 */

public class FakeBackend {

    private static class FakeUser {
        public String username;
        public String password;
        public List<String> sessions;

        public FakeUser(String username, String password, List<String> sessions) {
            this.username = username;
            this.password = password;
            this.sessions = sessions;
        }
    }

    private static List<FakeUser> users = Collections.synchronizedList(new ArrayList<FakeUser>());

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

    public static String login(String username, String password) {
        //TODO Add session to user if exists and return session, else return null
        return null;
    }

    public static void logout(String sessionID) {
        //TODO Clear session from user if exists
    }

    public static boolean register(String username, String password) {
        //TODO Add user
        return false;
    }
}
