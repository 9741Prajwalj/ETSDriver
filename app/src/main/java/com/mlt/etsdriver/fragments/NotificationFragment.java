package com.mlt.etsdriver.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mlt.etsdriver.R;
import com.mlt.etsdriver.adapters.NotificationAdapter;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    private RecyclerView recReview;
    private LinearLayout layoutNotification;
    private TextView noNotificationText;
    private ImageView backArrow;

    // Sample data for demonstration
    private ArrayList<String> notifications;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // Initialize views
        recReview = view.findViewById(R.id.recReview);
        layoutNotification = view.findViewById(R.id.layoutNotification);
        noNotificationText = view.findViewById(R.id.noNotificationText);
        backArrow = view.findViewById(R.id.backArrow);

        // Set up RecyclerView
        recReview.setLayoutManager(new LinearLayoutManager(getContext()));
        notifications = new ArrayList<>(); // Example notification list
        NotificationAdapter adapter = new NotificationAdapter(notifications);
        recReview.setAdapter(adapter);

        // Handle back arrow click
        backArrow.setOnClickListener(v -> getActivity().onBackPressed());

        // Check if notifications are available
        if (notifications.isEmpty()) {
            showNoNotifications();
        }

        return view;
    }

    private void showNoNotifications() {
        layoutNotification.setVisibility(View.VISIBLE);
        recReview.setVisibility(View.GONE);
    }
}
