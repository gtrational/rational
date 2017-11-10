package edu.gatech.cs2340.gtrational.rational.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

/**
 * A splash screen that determines whether a user is logged in and directs them to the correct screen.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
