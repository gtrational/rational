package edu.gatech.cs2340.gtrational.rational.controller;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import edu.gatech.cs2340.gtrational.rational.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void verifyLogin(View view) {
        EditText usernameField = (EditText) findViewById((R.id.editTextUsername));
        EditText passwordField = (EditText) findViewById((R.id.editTextPassword));

        EditText[] requiredFields = {usernameField, passwordField};
        String[] messages = {"Please enter username", "Please enter password"};

        //Display toast if one of the required fields is left blank
        for (int i = 0; i < requiredFields.length; i++) {
            EditText field = requiredFields[i];
            if (field.getText().toString().length() == 0) {
                int[] loc = new int[2];
                field.getLocationOnScreen(loc);

                Toast t = Toast.makeText(getApplicationContext(), messages[i], Toast.LENGTH_SHORT);

                //Display toast on right of screen at the y value of the input field
                t.setGravity(Gravity.TOP | Gravity.RIGHT, 0, loc[1] - field.getHeight() / 2 - 20);
                t.show();
                return;
            }
        }

        //Hide keyboard if open
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (username.equals("user") && password.equals("pass")) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Log.d("Tag", "YOU DUN MESSED UP AY AY RON");
            Snackbar snackbar = Snackbar.make(view, "Login Failed", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public void cancelLogin(View view) {
        finish();
    }
}
