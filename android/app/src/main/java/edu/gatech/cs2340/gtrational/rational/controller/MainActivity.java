package edu.gatech.cs2340.gtrational.rational.controller;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.RationalApp;
import edu.gatech.cs2340.gtrational.rational.model.Model;
import edu.gatech.cs2340.gtrational.rational.model.RationalConfig;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

public class MainActivity extends AppCompatActivity {

    private static final boolean WE_ARE_LAZY = RationalConfig.getSetting(RationalConfig.AREWELAZY).equals("yes");

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
        String username = "testuser";
        String password = "testpass";
        WebAPI.login(username, password, (WebAPI.LoginResult result) -> {
            if (result.success) {
                try {
                    Model.getInstance().updateUser(new JSONObject().put("email", username).put("sessionID", result.sessionID).put("permLevel", result.permissionLevel.ordinal()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(this, MainDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Log.d("Tag", "YOU DUN MESSED UP AY AY RON");
                Snackbar snackbar = Snackbar.make(view, "Login Failed", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }
}
