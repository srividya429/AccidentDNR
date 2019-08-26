package com.cyient.iot.ambulance;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.cyient.iot.ambulance.action.FOO";
    private static final String ACTION_BAZ = "com.cyient.iot.ambulance.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.cyient.iot.ambulance.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.cyient.iot.ambulance.extra.PARAM2";

    private Context context;
    private double mLatitude1 = 17.418665;
    private double mLongitude1 = 78.337568;

    // Default Constructor
    public MyIntentService() {
        super("MyIntentService");
    }

    // Dynamic Constructor
    public MyIntentService(Context context) {
        super("MyIntentService");
        this.context = context;
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private class GetArduinoDetails extends AsyncTask<Void, Integer, Integer> {

        private double latitude2;
        private double longitude2;
        private double distance;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                ManagerClass managerClass = new ManagerClass();
                CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(context);

                if (credentialsProvider != null) {
                    AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

                    ddbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
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
            // progressBar.setVisibility(View.INVISIBLE);
            if (integer == 1) {
                Log.i("MY SERVICE", "Successful.");
                // showToast("Successful.");
                // showToast("" + distance(latitude2, longitude2));
                // ((Button) findViewById(R.id.notificationBtn)).setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                /*Intent i = new Intent(ActionActivity.this, ActionActivity.class);
                i.putExtra("ID", mVehicleNo);
                startActivity(i);*/
            } else if (integer == 2) {
                Log.i("MY SERVICE", "Failed.");
                // showToast("failed.");
            } else {
                Log.i("MY SERVICE", "Unexpected Issue # :: " + integer);
                // showToast("Unexpected Issue #:: " + integer);
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
}