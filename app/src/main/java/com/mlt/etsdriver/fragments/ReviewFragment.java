package com.mlt.etsdriver.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mlt.etsdriver.R;
import com.mlt.etsdriver.activities.MainActivity;
import com.mlt.etsdriver.adapters.ReviewAdapter;

public class ReviewFragment extends Fragment {

    private RecyclerView recReview;
    private ImageView backArrow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_activity, container, false);

        // Initialize views
        recReview = view.findViewById(R.id.recReview);
        backArrow = view.findViewById(R.id.backArrow);

        // Set up the RecyclerView
        setupRecyclerView();

        // Set back arrow click listener
        backArrow.setOnClickListener(v -> {
            // Navigate back to MainActivity
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });


        return view;
    }

    private void setupRecyclerView() {
        // Create a layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recReview.setLayoutManager(layoutManager);

        // Set adapter for the RecyclerView
        // Replace `ReviewAdapter` with your actual adapter class
        ReviewAdapter adapter = new ReviewAdapter(); // Assume you have an adapter class
        recReview.setAdapter(adapter);
    }
}
