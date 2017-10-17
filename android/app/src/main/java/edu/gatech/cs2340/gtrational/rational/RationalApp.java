package edu.gatech.cs2340.gtrational.rational;

import android.app.Application;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert on 10/17/2017.
 */

public class RationalApp extends Application {

    private static RationalApp instance;

    public static RationalApp getInstance() {
        return instance;
    }

    public static final String HOSTURL = "hosturl";
    public static final String AREWELAZY = "arewelazy";

    private static final String[] requiredSettings = {HOSTURL, AREWELAZY};

    private Map<String, String> settings;

    public RationalApp() {
        instance = this;
        settings = new HashMap<>();
    }

    public String getSetting(String key) {
        if (!settings.containsKey(key)) {
            throw new RuntimeException("Setting \"" + key + "\" has not been loaded");
        }
        return settings.get(key);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadConfig();
        for (String check : requiredSettings) {
            if (!settings.containsKey(check)) {
                throw new RuntimeException("Setting value not found for \"" + check + "\". Make sure you run the GenSettings JUnit");
            }
        }
    }

    private void loadConfig() {
        int settingsId = -1;
        Class<?>[] classes = R.class.getClasses();
        for (Class<?> clazz : classes) {
            if (clazz.getName().contains("xml")) {
                try {
                    Field field = clazz.getField("settings");
                    settingsId = (int) field.get(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {}
            }
        }

        if (settingsId == -1) {
            return;
        }

        XmlResourceParser xrp = getApplicationContext().getResources().getXml(settingsId);

        try {
            int eventType;
            while ((eventType = xrp.next()) != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && xrp.getName().equalsIgnoreCase("setting")) {
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
