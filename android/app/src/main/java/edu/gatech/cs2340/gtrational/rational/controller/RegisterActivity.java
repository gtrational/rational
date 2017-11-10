package edu.gatech.cs2340.gtrational.rational.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.model.User;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

public class RegisterActivity extends AppCompatActivity {

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
        EditText usernamefield = (EditText) findViewById(R.id.editTextUsername_R);
        EditText passwordfield = (EditText) findViewById(R.id.editTextPassword_R);
        EditText confirmPasswordField = (EditText) findViewById(R.id.editTextPasswordConfirm_R);
        RadioButton admin_button = (RadioButton) findViewById(R.id.admin_button);
        RadioButton user_button = (RadioButton) findViewById(R.id.user_button);

        EditText[] requiredFields = {usernamefield, passwordfield, confirmPasswordField};
        String[] messages = {"Please enter username", "Please enter password", "Please confirm password"};


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
        View focused = getCurrentFocus();
        if (focused != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(focused.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        String username = usernamefield.getText().toString();
        String password = passwordfield.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if (password.equals(confirmPassword)) {
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
        } else {
            Snackbar snackbar = Snackbar.make(view, "Passwords don't match", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    /**
     * If user presses "cancel", registration is cancelled and activity finishes.
     * @param view The View object
     */
    public void cancelRegister(View view) {
        finish();
    }
}