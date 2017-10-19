package edu.gatech.cs2340.gtrational.rational;

import android.app.Application;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import edu.gatech.cs2340.gtrational.rational.model.RationalConfig;

/**
 * Created by Robert on 10/17/2017.
 */

public class RationalApp extends Application {

    private static RationalApp instance;

    public static RationalApp getInstance() {
        return instance;
    }

    private Context applicationContext;

    public RationalApp() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.applicationContext = getApplicationContext();
        RationalConfig.init();
    }
}
