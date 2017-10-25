package edu.gatech.cs2340.gtrational.rational.controller;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO apply filter
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO Cancelled
                    }
                })
                .setView(R.layout.fragment_map_filter);
        return builder.create();
    }
}
