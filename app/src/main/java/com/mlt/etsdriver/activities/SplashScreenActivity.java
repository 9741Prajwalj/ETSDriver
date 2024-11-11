package com.mlt.etsdriver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.mlt.etsdriver.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the main RelativeLayout
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        ));
        relativeLayout.setBackgroundColor(getResources().getColor(R.color.content_background));

        // Setting up the ImageView
        ImageView imageView = new ImageView(this);
        RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(
                895, // Width in dp
                895  // Height in dp
        );
        imageViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(imageViewParams);
        imageView.setImageResource(R.drawable.ic_launcher_web);

        // Adding the ImageView to the RelativeLayout
        relativeLayout.addView(imageView);

        // Setting up the ProgressBar
        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        RelativeLayout.LayoutParams progressBarParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                5 // Height in dp
        );
        progressBarParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        progressBar.setLayoutParams(progressBarParams);
        progressBar.setProgressBackgroundTintList(getResources().getColorStateList(R.color.white));
        progressBar.setProgressTintList(getResources().getColorStateList(R.color.colorAccent));
        progressBar.setVisibility(View.VISIBLE);

        // Adding the ProgressBar to the RelativeLayout
        relativeLayout.addView(progressBar);

        // Setting the RelativeLayout as the content view
        setContentView(relativeLayout);

        // Navigate to LoginActivity after 1 second
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: Finish the SplashScreenActivity so the user can't go back to it
            }
        }, 2000); // 2000 milliseconds = 2 second
    }
}
