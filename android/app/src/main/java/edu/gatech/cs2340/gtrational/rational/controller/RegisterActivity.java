package edu.gatech.cs2340.gtrational.rational.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.web.WebAPI;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Register");
        setContentView(R.layout.activity_register);

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

    public void verifyRegister(View view) {
        EditText usernamefield = (EditText) findViewById(R.id.editTextUsername_R);
        EditText passwordfield = (EditText) findViewById(R.id.editTextPassword_R);
        RadioButton admin_button = (RadioButton) findViewById(R.id.admin_button);
        RadioButton user_button = (RadioButton) findViewById(R.id.user_button);

        String username = usernamefield.getText().toString();
        String password = passwordfield.getText().toString();

        if (admin_button.isChecked()) {
            WebAPI.register(username , password);
        } else {
            WebAPI.register(username , password);
        }
    }

    public void cancelRegister(View view) {
        finish();
    }
}