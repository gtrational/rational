package edu.gatech.cs2340.gtrational.rational.controller;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.gatech.cs2340.gtrational.rational.R;

/**
 * A fragment for the dashboard screen.
 */
public class MapFilterDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Filter")
                .setPositiveButton("Apply", (DialogInterface dialog, int id) -> {
                    //Parse date fields
                    EditText startDateField = getDialog().findViewById((R.id.startDate));
                    EditText endDateField = getDialog().findViewById((R.id.endDate));
                    String startDate = startDateField.getText().toString();
                    String endDate = endDateField.getText().toString();

                    try {
                        long startLong = dateToSeconds(startDate);
                        long endLong = dateToSeconds(endDate);
                        ((MainDashboardActivity)getActivity()).setMapPins(startLong, endLong);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }

                })
                .setNegativeButton("Cancel", (DialogInterface dialog, int id) -> {
                })
                .setView(R.layout.fragment_map_filter);
        return builder.create();
    }

    /**
     * Converts date to seconds
     * @param date The date
     * @return The milliseconds
     * @throws ParseException The exception
     */
    public static long dateToSeconds(String date) throws ParseException {
        if (date == null) {
            throw new NullPointerException("Date cannot be null");
        }
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        Date actual_date = format.parse(date);
        return actual_date.getTime();
    }
}
