package edu.gatech.cs2340.gtrational.rational.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import edu.gatech.cs2340.gtrational.rational.R;

public class ViewDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        Intent prev = getIntent();
        Bundle bundle = prev.getExtras();
        Log.d("tag", bundle.getString("text"));

        setTitle("Rat Sighting #" + bundle.getString("text"));
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
}
