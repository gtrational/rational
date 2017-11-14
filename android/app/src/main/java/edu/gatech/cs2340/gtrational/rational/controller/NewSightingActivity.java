package edu.gatech.cs2340.gtrational.rational.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import edu.gatech.cs2340.gtrational.rational.R;
import edu.gatech.cs2340.gtrational.rational.model.web.WebAPI;

/**
 * A class for the new sighting activity
 */
public class NewSightingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_new_sighting);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);

        Spinner spinner = findViewById(R.id.location_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.location_type, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner boroughSpinner = findViewById(R.id.borough_spinner);
        ArrayAdapter<CharSequence> boroughAdapter = ArrayAdapter.createFromResource(
                this, R.array.boroughs, android.R.layout.simple_spinner_item
        );
        boroughAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boroughSpinner.setAdapter(boroughAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_sighting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                Spinner locationType = findViewById(R.id.location_type);
                EditText address1 = findViewById((R.id.address_line1));
                EditText address2 = findViewById((R.id.address_line2));
                EditText city = findViewById((R.id.city));
                EditText zip = findViewById((R.id.ZIP));
                Spinner borough = findViewById(R.id.borough_spinner);

                int zipCode;
                try {
                    zipCode = Integer.parseInt(zip.getText().toString());
                } catch (Exception e) {
                    // TODO Popup saying invalid zip code
                    return true;
                }

                String address = address1.getText().toString();
                String secondAddress = address2.getText().toString();
                if (!secondAddress.isEmpty()) {
                    address += secondAddress;
                }

                WebAPI.RatData newRatData = new WebAPI.RatData(
                        -1,
                        System.currentTimeMillis(),
                        locationType.getSelectedItem().toString(),
                        WebAPI.RatData.AddressInfo.of(
                                address,
                                city.getText().toString(),
                                borough.getSelectedItem().toString(),
                                zipCode,
                                new WebAPI.RatData.LatLon(0, 0)
                        )
                );

                WebAPI.addRatSighting(newRatData, (WebAPI.RatDataResult result) -> {
                    if (result.success) {
                        finish();
                    }
                });

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
