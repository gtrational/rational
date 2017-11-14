package edu.gatech.cs2340.gtrational.rational;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import edu.gatech.cs2340.gtrational.rational.model.Model;
import edu.gatech.cs2340.gtrational.rational.model.User;

import static org.junit.Assert.*;

/**
 * Test class for Model.UpdateUser
 * Created by daniel on 11/13/17.
 */

public class ModelUpdateUserTest {

    /**
     * Test behavior or Model.updateUser
     *
     * @throws JSONException jsonObjects
     */
    @Test
    public void testUpdateUser() throws JSONException {
        Model model = Model.getInstance();

        String email = "user@gmail.com";
        String sessionId = "AAAA";
        User.PermissionLevel permLevel = User.PermissionLevel.ADMIN;

        JSONObject newUser = new JSONObject()
                .put("email", "user@gmail.com")
                .put("sessionID", "AAAA")
                .put("permLevel", permLevel.ordinal());

        model.updateUser(newUser);

        User testUser = model.getUser();
        assertEquals(testUser.getEmail(), email);
        assertEquals(testUser.getSessionId(), sessionId);
        assertEquals(testUser.getPermissionLvl(), permLevel);

        model.updateUser(null);

        testUser = model.getUser();
        assertEquals(testUser.getEmail(), email);
        assertEquals(testUser.getSessionId(), sessionId);
        assertEquals(testUser.getPermissionLvl(), permLevel);

        JSONObject newFakeUser = new JSONObject().put("wrong", "wrong");

        model.updateUser(newFakeUser);

        testUser = model.getUser();
        assertEquals(testUser.getEmail(), email);
        assertEquals(testUser.getSessionId(), sessionId);
        assertEquals(testUser.getPermissionLvl(), permLevel);
    }


}
