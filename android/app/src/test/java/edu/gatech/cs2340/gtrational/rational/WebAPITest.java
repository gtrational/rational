package edu.gatech.cs2340.gtrational.rational;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import edu.gatech.cs2340.gtrational.rational.model.RationalConfig;
import edu.gatech.cs2340.gtrational.rational.model.User;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@SuppressWarnings({"AssignmentToStaticFieldFromInstanceMethod", "UseOfSystemOutOrSystemErr"})
public class WebAPITest {

    private static final int testPort = 8432;

    private static Map<String, String> httpServerResponse;

    /**
     * Set up testing
     */
    @BeforeClass
    public static void setup() {
        //Load config
        try {
            RationalConfig.init();
        } catch (Exception ignored) {
        }
        RationalConfig.setSetting("host_url", "http://localhost:" + testPort);

        //Setup web server
        httpServerResponse = new HashMap<>();
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(testPort), 0);
            server.createContext("/", (HttpExchange exchange) -> {
                String uri = exchange.getRequestURI().toString();
                String response = httpServerResponse.getOrDefault(uri, "");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            });
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static volatile WebAPI.LoginResult loginResult;

    private static void doLogin() {
        loginResult = null;
        String username = "testuser";
        String password = "testpass";
        WebAPI.login(username, password, (WebAPI.LoginResult res) -> loginResult = res);
    }

    private static void assertLoginResult(WebAPI.LoginResult expected, WebAPI.LoginResult actual) {
        Assert.assertEquals(expected.success, actual.success);
        Assert.assertEquals(expected.errMsg, actual.errMsg);
        Assert.assertEquals(expected.sessionID, actual.sessionID);
        Assert.assertEquals(expected.permissionLevel, actual.permissionLevel);
    }

    /**
     * Tests all login cases
     *
     * @throws InterruptedException from Thread.sleep()
     * @throws JSONException from json
     */
    @Test
    public void testLogin() throws InterruptedException, JSONException {
        String randomError = "this is a rAnD0m eRR";

        String[] payloads = {
                "lol",
                new JSONObject().put("err", randomError).toString(),
                new JSONObject().put("sessionid", "1234").put("permLevel", 0).toString(),
                new JSONObject().put("sessionid", "5678").put("permLevel", 1).toString()
        };

        WebAPI.LoginResult[] expected = {
                new WebAPI.LoginResult(false, "No server response", null, 0),
                new WebAPI.LoginResult(false, randomError, null, 0),
                new WebAPI.LoginResult(true, null, "1234", User.PermissionLevel.USER.ordinal()),
                new WebAPI.LoginResult(true, null, "5678", User.PermissionLevel.ADMIN.ordinal())
        };

        for (int i = 0; i < payloads.length; i++) {
            httpServerResponse.put("/api/login", payloads[i]);

            doLogin();

            while (loginResult == null) {
                Thread.sleep(1);
            }

            System.out.println("Testing login with payload " + payloads[i]);
            assertLoginResult(expected[i], loginResult);
        }

    }
}