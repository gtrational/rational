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
 */

public class RationalConfig {
    public static final String HOSTURL = "hosturl";
    public static final String AREWELAZY = "arewelazy";

    private static final String[] requiredSettings = {HOSTURL, AREWELAZY};

    private static Map<String, String> settings;

    public static void init() {
        settings = new HashMap<>();
        loadConfig();
        for (String check : requiredSettings) {
            if (!settings.containsKey(check)) {
                throw new RuntimeException("Setting value not found for \"" + check + "\". Make sure you run the GenSettings JUnit");
            }
        }
    }

    public static String getSetting(String key) {
        if (!settings.containsKey(key)) {
            throw new RuntimeException("Setting \"" + key + "\" has not been loaded");
        }
        return settings.get(key);
    }

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
            int eventType;
            while ((eventType = xrp.next()) != XmlPullParser.END_DOCUMENT) {
                if ((eventType == XmlPullParser.START_TAG) && "setting".equalsIgnoreCase(xrp.getName())) {
                    String setting = xrp.getAttributeValue(null, "name");
                    xrp.next();
                    settings.put(setting, xrp.getText());
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

}
