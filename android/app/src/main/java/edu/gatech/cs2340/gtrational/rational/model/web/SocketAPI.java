package edu.gatech.cs2340.gtrational.rational.model.web;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by shyamal on 10/16/17.
 */

public class SocketAPI {
    private static SocketAPI instance = new SocketAPI();
    private Socket socket;

    private SocketAPI() {
        try {
            socket = IO.socket("http://rational.tk");
            socket.connect();
            socket.on("update", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONArray arr = (JSONArray) args[0];
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            WebAPI.RatData ratOccurence = new WebAPI.RatData((JSONObject) arr.get(i));
                        } catch (JSONException e) {}
                    }
                }
            });
        } catch (URISyntaxException e) {}
    }

    public static SocketAPI getInstance() {
        return instance;
    }

    public void attemptSend(JSONObject data) {
        if (data == null) {
            return;
        }
        socket.emit("data", data);
    }
}
