package edu.gatech.cs2340.gtrational.rational.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import edu.gatech.cs2340.gtrational.rational.R;

public class MainActivity extends AppCompatActivity {

    private static final boolean WE_ARE_LAZY = false;

    /**
     * On create method
     * @param savedInstanceState Saved state object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!WE_ARE_LAZY) {
            findViewById(R.id.buttonLazy).setVisibility(View.GONE);
        }
    }

    /**
     * Switches to Login View
     * @param view
     */
    public void openLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * When James is lazy, he does things to make his life easier.
     * @param view
     */
    public void lazyLogin(View view) {
        // WebAPI.fetchPrelimRatData(); was trying to test 
        Intent intent = new Intent(this, MainDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
