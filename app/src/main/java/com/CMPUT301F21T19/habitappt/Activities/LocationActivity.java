/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: LocationActivity
 *
 * Description:
 * creates google map view for habit event, allowing user to select
 * given location to associate event to. Passes location data back to fragment to save to event
 *
 *
 * Note: referenced current place tutorial designed by google developers for google maps API info
 * Link: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
 * @version "%1%,%5%"
 *
 *
 */
package com.CMPUT301F21T19.habitappt.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.CMPUT301F21T19.habitappt.Fragments.EditEvent;
import com.CMPUT301F21T19.habitappt.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    /**
     * marker to place on map for user
     */
    private Marker marker;
    private GoogleMap map;

    /**
     * used to get latitude and longitude coordinates
     */
    private FusedLocationProviderClient fusedLocationClient;

    /**
     * various attributes associated to tracking using location/map settings
     */
    private final LatLng defaultLocation = new LatLng(0, 0);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private static final String KEY_CAMERA_POSITION = "base_position";

    private static final String KEY_LOCATION = "base_location";
    private static final String TAG = LocationActivity.class.getSimpleName();
    public static  final String MAPS_API_KEY="AIzaSyBsJjMX7Al622N4TNpGIqO_uCew_yTZz9s";


    /**
     * The geographical location where the device is currently located
     */
    private Location lastKnownLocation;

    /**
     * current camera position
     */
    private CameraPosition cameraPosition;

    String latitude, longitude;
    TextView latTextView, lonTextView;

    Button saveButton;
    double currLat, currLong;


    /**
     * initializes map view with marker centerd on users current location
     * @param savedInstanceState passsed instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.loaction_view);

        //setup text views
        latTextView = findViewById(R.id.current_lat);
        lonTextView = findViewById(R.id.current_lon);
        latTextView.setText("Current Latitude: Select location to calibrate") ;
        lonTextView.setText("Current Longitude: Select location to calibrate");

        //setup button
        saveButton = findViewById(R.id.save_button);

        //create location provider instance
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        // Build the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //if save now is selected by user, pass back current lat and long to edit event (to update event)
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass lat/lon back to EditEvent
                Intent returnToEditEvent = new Intent(getApplicationContext(), EditEvent.class);
                returnToEditEvent.putExtra("latitude", currLat);
                returnToEditEvent.putExtra("longitude", currLong);
                //send result back
                setResult(Activity.RESULT_OK, returnToEditEvent);
                finish();

            }
        });

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //initialize as false unless permissions granted
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * when map is ready, save context to the map, and mark current location once user
     * permissions are set
     * @param googleMap current map instance
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;

        //set on click listener
        this.map.setOnMapClickListener(this);

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        //check to see if we have permission to access location, if not, ask user for permission
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        //if no map exists, don update
        if (map == null) {
            return;
        }
        try {
            //if location permission granted, setup map settings. Dont otherwise
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);


            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.getUiSettings().setZoomControlsEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.

                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                //save context to lattitude and longitude
                                double lat = currLat = lastKnownLocation.getLatitude();
                                double lon = currLong = lastKnownLocation.getLongitude();

                                //move camera to location as found
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lat,
                                                lon), DEFAULT_ZOOM));
                                //add marker to exact point
                                marker = map.addMarker(new MarkerOptions().
                                        position(new LatLng(lat, lon)).title("Lat: " + lat + ", Lon: " + lon).draggable(true));

                            }
                        }
                        // if cannot get location, set map to base location
                        else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * When map is clicked, move marker/save context, also update textview
     */
    @Override
    public void onMapClick(LatLng latLng) {
        if(marker != null){
            marker.remove();
        }
        //move camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latLng.latitude,
                        latLng.longitude), DEFAULT_ZOOM));
        //recreate in new position
        marker = map.addMarker(new MarkerOptions().position(latLng)
                .title("Lat: " + latLng.latitude + ", Lon: " + latLng.longitude).draggable(true));

        //update last known location
        lastKnownLocation.setLatitude(latLng.latitude);
        lastKnownLocation.setLatitude(latLng.longitude);
        currLat = latLng.latitude;
        currLong = latLng.longitude;

        //update text view
        latTextView.setText("Current Latitude: " + (int)currLat + "\u00B0");
        lonTextView.setText("Current Longitude: " + (int)currLong + "\u00B0");

    }
}
