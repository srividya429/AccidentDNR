package com.cyient.iot.ambulance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActionActivity extends AppCompatActivity implements
        View.OnClickListener,
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private String mVehicleNo = "";
    private MapView mapView = null;
    private GoogleMap googleMap = null;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private ProgressBar progressBar = null;
    private double mLatitude1;
    private double mLongitude1;
    private Button mNotificationBtn;
    private Button mExitServiceBtn;
    private Intent serviceIntent;
    private boolean mNotificationArrived = false;
    private AmbulanceInfo mAmbulanceInfo = null;
    private SharedPreferences.Editor mSharedPreferences = null;
    private Button mUpdateStatusBtn = null;

    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);

    /**
     * Keeps track of the selected marker.
     */
    private Marker mSelectedMarker;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        // MapsInitializer.initialize(getApplicationContext());

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        // mapView = (MapView) findViewById(R.id.map);
        // mapView.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("DnrPrefsFile", MODE_PRIVATE);
        mSharedPreferences = preferences.edit();
        mVehicleNo = preferences.getString("VEHICLE_NO", "");
        initViews();
    }

    private void initViews() {
        //mLatitude1 = 17.418665;
        //mLongitude1 = 78.337568;
        mLatitude1 = -37.799802;
        mLongitude1 = 144.9565125;
        mNotificationBtn = (Button) findViewById(R.id.notificationBtn);
        mExitServiceBtn = (Button) findViewById(R.id.exitServiceBtn);
        mUpdateStatusBtn = (Button) findViewById(R.id.updateStatusBtn);
        serviceIntent = new Intent(ActionActivity.this, MyIntentService.class);
        /*if (mapView != null) {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mGoogleMap) {
                    googleMap = mGoogleMap;

                    // Add a marker in Sydney, Australia, and move the camera.
                    LatLng sydney = new LatLng(-34, 151);
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    *//*googleMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_flag))
                            .anchor(0.0f, 1.0f)
                            .position(new LatLng(mLatitude1, mLongitude1)));
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    if (ActivityCompat.checkSelfPermission(ActionActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(ActionActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    MapsInitializer.initialize(ActionActivity.this);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(new LatLng(mLatitude1, mLongitude1));
                    LatLngBounds bounds = builder.build();
                    int padding = 0;
                    // Updates the location and zoom of the MapView
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    googleMap.moveCamera(cameraUpdate);*//*
                }
            });
        }*/
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        new OnMapAndViewReadyListener(mapFragment, this);
        new GetArduinoDetails().execute();

        mNotificationBtn.setOnClickListener(this);
        mExitServiceBtn.setOnClickListener(this);
        mUpdateStatusBtn.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notificationBtn:
                if (mNotificationArrived) {
                    mNotificationArrived = false;
                    AlertDialog dialog = new AlertDialog.Builder(ActionActivity.this)
                            .setCancelable(true).setMessage("Are you going to accept this ?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mNotificationBtn.clearAnimation();
                                    mAmbulanceInfo = new AmbulanceInfo();
                                    mAmbulanceInfo.setVehicle_no(mVehicleNo);
                                    mAmbulanceInfo.setLatitude(String.valueOf(mLatitude1));
                                    mAmbulanceInfo.setLongitude(String.valueOf(mLongitude1));
                                    mAmbulanceInfo.setStatus("ACTIVE");
                                    mAmbulanceInfo.setGen_time(String.valueOf(System.currentTimeMillis()));
                                    new UpdateAmbulanceInfoStatus().execute("ACTIVE");
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create();
                    dialog.show();
                }
                break;
            case R.id.exitServiceBtn:
                mAmbulanceInfo = new AmbulanceInfo();
                mAmbulanceInfo.setVehicle_no(mVehicleNo);
                mAmbulanceInfo.setLatitude(String.valueOf(mLatitude1));
                mAmbulanceInfo.setLongitude(String.valueOf(mLongitude1));
                mAmbulanceInfo.setStatus("INACTIVE");
                mAmbulanceInfo.setGen_time(String.valueOf(System.currentTimeMillis()));
                new UpdateAmbulanceInfoStatus().execute("EXIT");
                stopService(new Intent(serviceIntent));
                break;
            case R.id.updateStatusBtn:
                mAmbulanceInfo = new AmbulanceInfo();
                mAmbulanceInfo.setVehicle_no(mVehicleNo);
                mAmbulanceInfo.setLatitude(String.valueOf(mLatitude1));
                mAmbulanceInfo.setLongitude(String.valueOf(mLongitude1));
                mAmbulanceInfo.setStatus("INACTIVE");
                mAmbulanceInfo.setGen_time(String.valueOf(System.currentTimeMillis()));
                new UpdateAmbulanceInfoStatus().execute("EXIT");

                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap mGoogleMap) {
        googleMap = mGoogleMap;

        //googleMap.setOnMyLocationButtonClickListener(this);
        //googleMap.setOnMyLocationClickListener(this);
        //enableMyLocation();

        //mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        // Hide the zoom controls.
        googleMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        addMarkersToMap();

        // Set listener for marker click event.  See the bottom of this class for its behavior.
        googleMap.setOnMarkerClickListener(this);

        // Set listener for map click event.  See the bottom of this class for its behavior.
        googleMap.setOnMapClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localized.
        googleMap.setContentDescription("Demo showing how to close the info window when the currently"
                + " selected marker is re-tapped.");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(MELBOURNE)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (googleMap != null) {
            // Access to the location has been granted to the app.
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void addMarkersToMap() {
        googleMap.addMarker(new MarkerOptions()
                .position(BRISBANE)
                .title("Brisbane")
                .snippet("Population: 2,074,200"));

        googleMap.addMarker(new MarkerOptions()
                .position(SYDNEY)
                .title("Sydney")
                .snippet("Population: 4,627,300"));

        googleMap.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title("Melbourne")
                .snippet("Population: 4,137,400"));

        googleMap.addMarker(new MarkerOptions()
                .position(PERTH)
                .title("Perth")
                .snippet("Population: 1,738,800"));

        googleMap.addMarker(new MarkerOptions()
                .position(ADELAIDE)
                .title("Adelaide")
                .snippet("Population: 1,213,000"));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mSelectedMarker = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // The user has re-tapped on the marker which was already showing an info window.
        if (marker.equals(mSelectedMarker)) {
            // The showing info window has already been closed - that's the first thing to happen
            // when any marker is clicked.
            // Return true to indicate we have consumed the event and that we do not want the
            // the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).
            mSelectedMarker = null;
            return true;
        }

        mSelectedMarker = marker;

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur.
        return false;
    }

    private class GetArduinoDetails extends AsyncTask<String, Integer, Integer> {

        private double latitude2;
        private double longitude2;
        private double distance;
        private String mUpdateStatus = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                ManagerClass managerClass = new ManagerClass();
                credentialsProvider = managerClass.getCredentials(ActionActivity.this);

                if (credentialsProvider != null) {
                    AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

                    ddbClient.setRegion(Region.getRegion(Regions.US_EAST_2));
                    DynamoDBMapper dynamoDBMapper = managerClass.initDynamoClient(credentialsProvider);

                    System.out.println("FindBicyclesOfSpecificTypeWithMultipleThreads: Scan Accident vehicles With Multiple Threads.");
                    Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
                    eav.put(":val1", new AttributeValue().withS("y"));

                    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                            .withFilterExpression("accident = :val1").withExpressionAttributeValues(eav);

                    List<DNRVehicleDetails> scanResult = dynamoDBMapper.scan(DNRVehicleDetails.class, scanExpression);
                    for (DNRVehicleDetails vehicle : scanResult) {
                    /*for (int i = 0; i < scanResult.size(); i++) {
                        DNRVehicleDetails vehicle = scanResult.get(i);*/
                        System.out.println(vehicle.getVehicle_no() + "== Latitude :: " + vehicle.getLatitude() + "== Longitude :: " + vehicle.getLongitude());
                        latitude2 = Double.parseDouble(vehicle.getLatitude());
                        longitude2 = Double.parseDouble(vehicle.getLongitude());
                        distance = distance(latitude2, longitude2);
                        if (distance < 5) {
                            break;
                        }
                    }
                    return 1;
                } else {
                    return 2;
                }
            } catch (AmazonServiceException ase) {
                ase.printStackTrace();
                return 3;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressBar.setVisibility(View.INVISIBLE);
            if (integer == 1) {
                showToast("Successful.");
                showToast("" + distance(latitude2, longitude2));
                final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
                animation.setDuration(500); // duration - half a second
                animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
                animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
                // mNotificationBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                mNotificationArrived = true;
                mNotificationBtn.startAnimation(animation);
                /*Intent i = new Intent(ActionActivity.this, ActionActivity.class);
                i.putExtra("ID", mVehicleNo);
                startActivity(i);*/
            } else if (integer == 2) {
                showToast("Failed.");
            } else {
                showToast("Unexpected Issue #:: " + integer);
            }
        }
    }

    private double distance(double lat2, double lon2) {
        double theta = mLongitude1 - lon2;
        double dist = Math.sin(deg2rad(mLatitude1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(mLatitude1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void showToast(String message) {
        Toast.makeText(ActionActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private class UpdateAmbulanceInfoStatus extends AsyncTask<String, Integer, Integer> {

        private String mUpdateStatus = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            mUpdateStatus = params[0];
            try {
                ManagerClass managerClass = new ManagerClass();
                credentialsProvider = managerClass.getCredentials(ActionActivity.this);


                if (credentialsProvider != null) {
                    DynamoDBMapper dynamoDBMapper = managerClass.initDynamoClient(credentialsProvider);
                    dynamoDBMapper.save(mAmbulanceInfo);
                } else {
                    return 2;
                }
                return 1;
            } catch (AmazonServiceException ase) {
                ase.printStackTrace();
                return 3;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressBar.setVisibility(View.INVISIBLE);
            if (integer == 1) {
                showToast("Successful.");
                if (mUpdateStatus.equalsIgnoreCase("EXIT")) {
                    mSharedPreferences.clear();
                    mSharedPreferences.commit();
                    startActivity(new Intent(ActionActivity.this, SignUpActivity.class));
                    finish();
                } else if (mUpdateStatus.equalsIgnoreCase("ACTIVE")) {

                }
            } else if (integer == 2) {
                showToast("Failed.");
            } else {
                showToast("Unexpected Issue #:: " + integer);
            }
        }
    }
}