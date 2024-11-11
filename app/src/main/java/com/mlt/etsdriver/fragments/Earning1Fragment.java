package com.mlt.etsdriver.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mlt.etsdriver.R;
import com.mlt.etsdriver.activities.MainActivity;

public class Earning1Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private LinearLayout lnrTitle;
    private ImageView backArrow;
    private TextView lblTarget;
    private TextView lblEarnings;
    private RecyclerView rcvRides;
    private RelativeLayout errorLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_earning1, container, false);

        // Initialize views
        lnrTitle = view.findViewById(R.id.lnrTitle);
        backArrow = view.findViewById(R.id.backArrow);
        lblTarget = view.findViewById(R.id.lblTarget);
        lblEarnings = view.findViewById(R.id.lblEarnings);
        rcvRides = view.findViewById(R.id.rcvRides);
        errorLayout = view.findViewById(R.id.errorLayout);

        // Set click listener for the back arrow
        // Set click listener for the back arrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity using an explicit Intent
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Clear the back stack
                startActivity(intent);
//                return false;
            }
        });


        // Additional setup (like RecyclerView adapter) can be done here
        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        // Set up the RecyclerView (adapter, layout manager, etc.)
        // For example:
        // rcvRides.setLayoutManager(new LinearLayoutManager(getContext()));
        // rcvRides.setAdapter(new YourAdapter());
    }

    // Additional methods for handling data can be added here
}