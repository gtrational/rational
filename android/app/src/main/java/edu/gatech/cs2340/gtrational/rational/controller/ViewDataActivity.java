package edu.gatech.cs2340.gtrational.rational.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.model.Model;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

/**
 * Class for view data activity
 */
public class ViewDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        Intent prev = getIntent();
        Bundle bundle = prev.getExtras();
        Log.d("tag", bundle.getString("text"));

        setTitle("Rat Sighting #" + bundle.getString("text"));

        WebAPI.RatData data = Model.getInstance().getRatDataByKey(
                Integer.parseInt(bundle.getString("text"))
        );

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss aa", Locale.US);

        TextView createdDate = (TextView) findViewById(R.id.created_date);
        createdDate.setText(sdf.format(data.createdTime));
        TextView locationType = (TextView) findViewById(R.id.location_type);
        locationType.setText(data.locationType);
        TextView zip = (TextView) findViewById(R.id.zip);
        zip.setText(data.incidentZip + "");
        TextView address = (TextView) findViewById(R.id.address);
        address.setText(data.incidentAddress);
        TextView city = (TextView) findViewById(R.id.city);
        city.setText(data.city);
        TextView borough = (TextView) findViewById(R.id.borough);
        borough.setText(data.borough);
        TextView latitude = (TextView) findViewById(R.id.latitude);
        latitude.setText(data.latitude + "");
        TextView longitude = (TextView) findViewById(R.id.longitude);
        longitude.setText(data.longitude + "");
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
