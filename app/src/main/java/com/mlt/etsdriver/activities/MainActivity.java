package com.mlt.etsdriver.activities;

import static androidx.core.app.PendingIntentCompat.getActivity;
import static com.mlt.etsdriver.manager.UserStatusManager.context;
import static com.mlt.etsdriver.network.FirebaseHelper.updateStatusInFirebase;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.mlt.etsdriver.R;
import com.mlt.etsdriver.fragments.Earning1Fragment;
import com.mlt.etsdriver.fragments.HelpFragment;
import com.mlt.etsdriver.fragments.HistoryFragment;
import com.mlt.etsdriver.fragments.NotificationFragment;
import com.mlt.etsdriver.fragments.ReviewFragment;
import com.mlt.etsdriver.fragments.EditProfileFragment;
import com.mlt.etsdriver.fragments.SummaryFragment;
import com.mlt.etsdriver.fragments.MapFragment;
import com.mlt.etsdriver.manager.UserStatusManager;
import com.mlt.etsdriver.utills.SharedPreferencesManager;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private  String Tag =" MainActivity";
    private NavigationView navigationView;
    private TextView txtEarning, txtReview, txtHistory, txtSummary, txtHelp, txtNotifi, txtEdituser;
    private Button btnLogout;
    private ActionBarDrawerToggle drawerToggle;
    private FragmentManager fragmentManager;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferencesManager
        sharedPreferencesManager = new SharedPreferencesManager(this);

        // Set up Toolbar
        MaterialToolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Initialize DrawerLayout, NavigationView, and FragmentManager
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fragmentManager = getSupportFragmentManager();

        // Set up DrawerToggle
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize TextViews and Button
        txtEdituser = navigationView.findViewById(R.id.txtEdituser);
        txtEarning = navigationView.findViewById(R.id.txtEarning);
        txtReview = navigationView.findViewById(R.id.txtReview);
        txtHistory = navigationView.findViewById(R.id.txtHistory);
        txtSummary = navigationView.findViewById(R.id.txtSummary);
        txtHelp = navigationView.findViewById(R.id.txtHelp);
        txtNotifi = navigationView.findViewById(R.id.txtNotifi);
        btnLogout = navigationView.findViewById(R.id.btnLogout);

//         Set up the logout button click listener
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Main Activity"," i have clicked the logout buttton");
                showLogoutConfirmationDialog();
            }
        });

        txtEdituser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new EditProfileFragment(), "Edit User");
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
            }
        });
        // Set up fragment transactions for each TextView
        txtNotifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new NotificationFragment(), "Notification");
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
            }
        });

        txtEarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Earning1Fragment(), "Earnings");
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
            }
        });

        txtReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new ReviewFragment(), "Review");
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
            }
        });

        txtHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HistoryFragment(), "History");
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
            }
        });

        txtSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new SummaryFragment(), "Summary");
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
            }
        });

        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HelpFragment(), "Help");
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
            }
        });

        // Load the initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MapFragment())
                    .commit();
        }
    }
    private void openFragment(Fragment fragment, String tag) {
        replaceFragment(fragment, tag);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
//     Method to show logout confirmation dialog
    private void showLogoutConfirmationDialog() {
        // Inflate the custom layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View dialogView = inflater.inflate(R.layout.logout_confirmation, null);

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        // Get references to the buttons in the custom layout
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            handleLogout();
        });
        dialog.show();
    }

    // Method to handle logout process
    private void handleLogout() {
        // Log the method entry for debugging
        Log.d("MainActivity", "Entered handleLogout method");

        // Get the context directly using MainActivity.this
        Context context = MainActivity.this;

        // Get user data from SharedPreferences
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        int userId = sharedPreferencesManager.getUserId();
        sharedPreferencesManager.clearLoginData();

        // Update status in backend and Firebase
        int status = 0;
        UserStatusManager.updateStatus(userId, status);
//        updateStatusInFirebase(userId, status);

        // Check if the context is an instance of Activity to safely cast it
        if (context instanceof Activity) {
            // Create an Intent to start LoginActivity
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);

            // Show a confirmation toast
            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Close the app or activity
            ((Activity) context).finishAffinity();
        } else {
            Log.e("Logout", "Context is not an instance of Activity, unable to start activity.");
        }
    }

    // Method to replace fragment with optional tag for backstack
    public void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (tag != null) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

