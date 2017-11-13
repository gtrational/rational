package edu.gatech.cs2340.gtrational.rational;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import edu.gatech.cs2340.gtrational.rational.model.Model;
import edu.gatech.cs2340.gtrational.rational.model.RationalConfig;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class WebAPITest {

    private static String sessionID;

    @BeforeClass
    public static void setup() {
        try {
            RationalConfig.init();
        } catch (Exception ignored) {}
        RationalConfig.setSetting("host_url", "http://localhost:8081");
    }


    private static volatile WebAPI.LoginResult loginResult;

    @Test
    public void testLogin() throws Exception {

        String username = "testuser";
        String password = "testpass";

        WebAPI.login(username, password, (WebAPI.LoginResult res) -> loginResult = res);

        while (loginResult == null) {
            Thread.sleep(1);
        }

        if (loginResult.success) {
            sessionID = loginResult.sessionID;
            Model.getInstance().updateUser(new JSONObject().put("email", username).put("sessionID", loginResult.sessionID).put("permLevel", loginResult.permissionLevel.ordinal()));
            System.out.println("Login Success; SessionID: " + sessionID);
        } else {
            System.out.println("Login Error: " + loginResult.errMsg);
        }
    }

    private static volatile List<WebAPI.RatData> getRatSightingsResult;

    @Test
    public void testGetRatSightings() throws Exception {
        if (sessionID == null) {
            testLogin();
        }

        WebAPI.getRatSightings(0, 5, (List<WebAPI.RatData> dat) -> getRatSightingsResult = dat);

        while (getRatSightingsResult == null) {
            Thread.sleep(1);
        }

        System.out.println("Got " + getRatSightingsResult.size() + " rat sightings:");
        for (WebAPI.RatData data : getRatSightingsResult) {
            System.out.println(data.toJson().toString(2));
        }
    }
}