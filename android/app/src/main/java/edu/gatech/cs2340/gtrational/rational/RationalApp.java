package edu.gatech.cs2340.gtrational.rational;

import android.app.Application;

import edu.gatech.cs2340.gtrational.rational.model.RationalConfig;

/**
 * Created by Robert on 10/17/2017.
 * Home base
 */

public class RationalApp extends Application {

    private static RationalApp instance;

    /**
     * Returns the instance of rational app
     * @return The instance
     */
    public static RationalApp getInstance() {
        return instance;
    }

    private static void setInstance(RationalApp instance) {
        RationalApp.instance = instance;
    }

    /**
     * Default constructor
     */
    public RationalApp() {
        RationalApp.setInstance(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RationalConfig.init();
    }
}
