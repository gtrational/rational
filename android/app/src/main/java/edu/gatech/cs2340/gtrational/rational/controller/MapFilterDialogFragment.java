package edu.gatech.cs2340.gtrational.rational.controller;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.gatech.cs2340.gtrational.rational.R;

/**
 * A fragment for the dashboard screen.
 */
public class MapFilterDialogFragment extends DialogFragment {

    public MapFilterDialogFragment() {
        // Required empty public constructor
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Filter")
                .setPositiveButton("Apply", (DialogInterface dialog, int id) -> {
                    //Parse date fields
                    EditText startDateField = getDialog().findViewById((R.id.startDate));
                    EditText endDateField = getDialog().findViewById((R.id.endDate));
                    String startDate = startDateField.getText().toString();
                    String endDate = endDateField.getText().toString();
                    //TODO call to long method
                    long startLong = 0;
                    long endLong = 0;
                    ((MainDashboardActivity)getActivity()).setMapPins(startLong, endLong);
                })
                .setNegativeButton("Cancel", (DialogInterface dialog, int id) -> {
                    // TODO Cancelled
                })
                .setView(R.layout.fragment_map_filter);
        return builder.create();
    }

    public static long dateToSeconds(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("mm-dd-yyyy");
        Date actual_date = new Date();

        actual_date = format.parse(date);
        return actual_date.getTime();
    }
}
