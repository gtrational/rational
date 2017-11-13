package edu.gatech.cs2340.gtrational.rational.model;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.RationalApp;

/**
 * Created by Robert on 10/18/2017.
 * Class to hold config information
 */

public class RationalConfig {
    public static final String HOST_URL = "host_url";
    public static final String ARE_WE_LAZY = "are_we_lazy";

    private static final String[] requiredSettings = {HOST_URL, ARE_WE_LAZY};

    private static Map<String, String> settings;

    /**
     * Method to init the config
     */
    public static void init() {
        settings = new HashMap<>();
        loadConfig();
        for (String check : requiredSettings) {
            if (!settings.containsKey(check)) {
                throw new RuntimeException("Setting value not found for \"" + check + "\". Make sure you run the GenSettings JUnit");
            }
        }
    }

    /**
     * Returns the setting
     * @param key The key
     * @return The setting value
     */
    public static String getSetting(String key) {
        if (!settings.containsKey(key)) {
            throw new RuntimeException("Setting \"" + key + "\" has not been loaded");
        }
        return settings.get(key);
    }

    /**
     * Sets a setting
     * @param key The key
     * @param value The new setting value
     */
    public static void setSetting(String key, String value) {
        settings.put(key, value);
    }

    private static void loadConfig() {
        int settingsId = -1;
        Class<?>[] classes = R.class.getClasses();
        for (Class<?> clazz : classes) {
            if (clazz.getName().contains("xml")) {
                try {
                    Field field = clazz.getField("settings");
                    settingsId = (int) field.get(null);
                } catch (NoSuchFieldException | IllegalAccessException ignored) {}
            }
        }

        if (settingsId == -1) {
            return;
        }

        XmlResourceParser xrp = RationalApp.getInstance().getApplicationContext().getResources().getXml(settingsId);

        try {
            int eventType = xrp.next();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if ((eventType == XmlPullParser.START_TAG) && "setting".equalsIgnoreCase(xrp.getName())) {
                    String setting = xrp.getAttributeValue(null, "name");
                    xrp.next();
                    settings.put(setting, xrp.getText());
                }
                eventType = xrp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

}
