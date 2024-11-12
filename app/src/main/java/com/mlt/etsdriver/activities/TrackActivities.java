package com.mlt.etsdriver.activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mlt.etsdriver.R;
import com.mlt.etsdriver.models.RouteResponse;
import com.mlt.etsdriver.network.ApiService;
import com.mlt.etsdriver.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class TrackActivities extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng currentLocation;
    private final LatLng destinationLocation = new LatLng(37.7749, -122.4194);
    private ApiService apiService;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private SharedPreferences sharedPreferences;
    private DatabaseReference firebaseDatabaseRef;
    private final String apiKey = "AIzaSyCI7CwlYJ6Qt5pQGW--inSsJmdEManW-K0"; // Replace with your actual API key
    private Polyline currentPolyline;
    private List<LatLng> routePoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track); // Make sure layout file is `activity_track.xml`

        // Initialize Firebase database reference
        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference("drivers");

        // Set up Retrofit API Service
        apiService = RetrofitClient.getApiService();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        // Set up Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.webView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Fetch current location from SharedPreferences
        currentLocation = getCurrentLocationFromPreferences();

        // Update location to Firebase and backend
        updateLocationToFirebaseAndBackend(currentLocation);
        // Set up FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("MapFragment", "Location permissions not granted");
            return;
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // Update every 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    updatePolyline(currentLocation);
                    updateMapLocation(currentLocation);
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }
    // Get current location from SharedPreferences
    private LatLng getCurrentLocationFromPreferences() {
        double lat = sharedPreferences.getFloat("current_lat", 0);
        double lng = sharedPreferences.getFloat("current_lng", 0);
        return new LatLng(lat, lng);
    }

    // Method to update location to Firebase and Backend
    private void updateLocationToFirebaseAndBackend(LatLng location) {
        // Update Firebase
        firebaseDatabaseRef.child("location").setValue(location)
                .addOnSuccessListener(aVoid -> {
                    // Firebase location updated successfully
                    Log.d("FirebaseUpdate", "Location updated in Firebase successfully.");
                })
                .addOnFailureListener(e -> {
                    // Handle Firebase update failure
                    Log.e("FirebaseUpdate", "Failed to update location in Firebase: " + e.getMessage());
                });


        // Update backend using Retrofit API
        apiService.updateLocation(location.latitude, location.longitude).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Location updated successfully in the backend
                    Log.d("BackendUpdate", "Location updated in backend successfully.");
                } else {
                    // Handle unsuccessful response
                    Log.e("BackendUpdate", "Failed to update location in backend: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure in backend update
                Log.e("BackendUpdate", "Error updating location in backend: " + t.getMessage());
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add marker at the current location and move camera
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

        // Add marker for destination
        mMap.addMarker(new MarkerOptions().position(destinationLocation).title("Destination"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLocation, 15));
        // Draw route from current location to destination
        drawRoute();
    }

    private void updatePolyline(LatLng currentLocation) {
        // Clear existing polylines to prevent overlapping
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(destinationLocation).title("Destination"));

        routePoints.add(currentLocation);
        // Request the route from the backend or use a static polyline if backend route calculation is unavailable
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(routePoints)
                .width(10)
                .color(Color.BLUE);

        mMap.addPolyline(polylineOptions);
        updateLocationToFirebaseAndBackend(currentLocation);

    }

    private void updateMapLocation(LatLng currentLocation) {
        // Move the camera to the new location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
    }

    // Method to draw route using the Google Directions API
    private void drawRoute() {
        String origin = currentLocation.latitude + "," + currentLocation.longitude;
        String destination = destinationLocation.latitude + "," + destinationLocation.longitude;

        // Use Google Directions API to get the route data
        RetrofitClient.getGoogleApiService().getRoute(origin, destination, apiKey).enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getRoutes().isEmpty()) {
                    String encodedPolyline = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                    routePoints = decodePolyline(encodedPolyline);

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(routePoints)
                            .width(10)
                            .color(Color.BLUE);
                    currentPolyline = mMap.addPolyline(polylineOptions);
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                // Handle failure
                Log.e("RouteError", "Failed to get route: " + t.getMessage());
            }
        });
    }

    // Method to decode polyline points
    private List<LatLng> decodePolyline(String encodedPolyline) {
        List<LatLng> polyline = new ArrayList<>();
        int index = 0, len = encodedPolyline.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encodedPolyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encodedPolyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng point = new LatLng(lat / 1E5, lng / 1E5);
            polyline.add(point);
        }
        return polyline;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fusedLocationProviderClient != null && locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }
}

