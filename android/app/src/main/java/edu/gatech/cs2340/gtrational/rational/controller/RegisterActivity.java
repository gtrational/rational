package edu.gatech.cs2340.gtrational.rational.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.model.User;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

/**
 * Class for register activity
 */
public class RegisterActivity extends AppCompatActivity {

    private static final int GRAVITY_MAGIC_NUMBER = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

    /**
     * Verifies that inputs user entered in make valid registration.
     * @param view The View object
     */
    public void verifyRegister(View view) {
        EditText usernameField = findViewById(R.id.editTextUsername_R);
        EditText passwordField = findViewById(R.id.editTextPassword_R);
        EditText confirmPasswordField = findViewById(R.id.editTextPasswordConfirm_R);

        EditText[] requiredFields = {usernameField, passwordField, confirmPasswordField};
        String[] messages = {
                "Please enter username",
                "Please enter password",
                "Please confirm password"
        };

        String[] fields = new String[3];
        fields[0] = usernameField.getText().toString();
        fields[1] = passwordField.getText().toString();
        fields[2] = confirmPasswordField.getText().toString();

        String answer = verifyRegister(fields, messages);
        int index = verifyRegister(requiredFields);

        if (!answer.equals(null) && !answer.equals("Success")) {
            int[] loc = new int[2];
            EditText field = requiredFields[index];
            field.getLocationOnScreen(loc);

            Toast t = Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_SHORT);

            //Display toast on right of screen at the y value of the input field
            t.setGravity(
                    Gravity.TOP | Gravity.RIGHT,
                    0,
                    loc[1] - (field.getHeight() / 2) - GRAVITY_MAGIC_NUMBER
            );
            t.show();
            return;
        } else if (answer.equals(null)) {
            Snackbar snackbar = Snackbar.make(view, "Passwords don't match", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (answer.equals("Success")) {
            tryRegister(view, fields[0], fields[1]);
        }

        //Hide keyboard if open
        View focused = getCurrentFocus();
        if (focused != null) {
            InputMethodManager inputManager
                    = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(
                        focused.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS
                );
            }
        }
    }

    /**
     *
     * @param fields
     * @param messages
     * @return
     */
    public static String verifyRegister(String[] fields, String[] messages) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isEmpty()) {
                return messages[i];
            }
        }

        if (!fields[2].equals(fields[3])) {
            return null;
        }

        return "Success";
    }

    public int verifyRegister(EditText[] reqfields) {
        for (int i = 0; i < reqfields.length; i++) {
            if (reqfields[i].getText().toString().isEmpty()) {
                return i;
            }
        }
        return 0;
    }

    private void tryRegister(View view, String username, String password) {
        Checkable admin_button = (RadioButton) findViewById(R.id.admin_button);

        WebAPI.register(
                username,
                password,
                admin_button.isChecked() ? User.PermissionLevel.ADMIN : User.PermissionLevel.USER,
                (WebAPI.RegisterResult res) -> {
                    if (res.success) {
                        finish();
                        return;
                    }

                    Snackbar snackbar = Snackbar.make(view, res.errMsg, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
        );
    }

    /**
     * If user presses "cancel", registration is cancelled and activity finishes.
     * @param view the view
     */
    public void cancelRegister(View view) {
        finish();
    }
}