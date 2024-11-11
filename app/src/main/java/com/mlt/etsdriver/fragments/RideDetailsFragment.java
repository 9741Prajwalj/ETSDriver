package com.mlt.etsdriver.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mlt.etsdriver.R;

public class RideDetailsFragment extends Fragment {

    private TextView txtDateTime;
    private TextView txtSource;
    private TextView txtDestination;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_history_list_item, container, false); // Ensure this matches your XML layout file name

        // Initialize TextViews
        txtDateTime = view.findViewById(R.id.datetime);
        txtSource = view.findViewById(R.id.txtSource);
        txtDestination = view.findViewById(R.id.txtDestination);

        // Load ride details (this could be from a database, API, or hardcoded for now)
        loadRideDetails();

        return view;
    }

    private void loadRideDetails() {
        // For demonstration, using hardcoded values. You can replace these with real data.
        String dateTime = "January 10 - 12:30 PM";
        String source = "Chhattarpur Road";
        String destination = "Home";

        // Set the values to the TextViews
        txtDateTime.setText(dateTime);
        txtSource.setText(source);
        txtDestination.setText(destination);
    }
}
