package com.mlt.etsdriver;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class ETSdriver extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this); // Initialize Firebase
    }
}

