package edu.gatech.cs2340.rational.rational;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import static edu.gatech.cs2340.rational.rational.Login.checkCredentials;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            FileInputStream fis = openFileInput(getString(R.string.login_path));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String username = reader.readLine();
            String password = reader.readLine();
            reader.close();
            if (checkCredentials(username, password)) {
                Intent intent = new Intent(this, Dashboard.class);
                intent.putExtra("userID", username);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            };
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
    }

    public void openLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void openRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
