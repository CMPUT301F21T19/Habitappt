package com.CMPUT301F21T19.habitappt;

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

    private Marker marker;
    private GoogleMap map;

    //location ref
    private FusedLocationProviderClient fusedLocationClient;

    // A default location and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(0, 0);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;


    // The geographical location where the device is currently located
    private Location lastKnownLocation;

    private CameraPosition cameraPosition;

    private static final String KEY_CAMERA_POSITION = "base_position";
    private static final String KEY_LOCATION = "base_location";

    private static final String TAG = LocationActivity.class.getSimpleName();

    public static  final String MAPS_API_KEY="AIzaSyBsJjMX7Al622N4TNpGIqO_uCew_yTZz9s";
    String latitude, longitude;
    TextView latTextView, lonTextView;

    Button saveButton;
    double currLat, currLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup text views
        latTextView = findViewById(R.id.current_lat);
        lonTextView = findViewById(R.id.current_lon);

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





        //setup button
        saveButton = findViewById(R.id.save_button);

        //create location provider instance
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        latTextView.setText("Current Latitude: Select location to calibrate") ;
        lonTextView.setText("Current Longitude: Select location to calibrate");

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //if save now is selected by user, pass back current lat and long to edit event (to update event)
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass lat/lon back to edit_event
                Intent returnToEditEvent = new Intent(getApplicationContext(), edit_event.class);
                returnToEditEvent.putExtra("latitude", currLat);
                returnToEditEvent.putExtra("longitude", currLong);
                setResult(Activity.RESULT_OK, returnToEditEvent);
                finish();

            }
        });

    }


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
        //check to see if we have permission to access location, if not, ask
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
        if (map == null) {
            return;
        }
        try {
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
                                double lat = currLat = lastKnownLocation.getLatitude();
                                double lon = currLong = lastKnownLocation.getLongitude();
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lat,
                                                lon), DEFAULT_ZOOM));
                                marker = map.addMarker(new MarkerOptions().
                                        position(new LatLng(lat, lon)).title("Lat: " + lat + ", Lon: " + lon).draggable(true));

                            }
                        } else {
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
