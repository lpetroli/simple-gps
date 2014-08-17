package com.lpetroli.simplegps.ui.fragments;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lpetroli.simplegps.R;

public class MapsFragment extends MapFragment implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "MapsFragment";
    public static final String MAPS_TAB_NAME = "GPS";

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final float MAP_ZOOM = 15;

    private GoogleMap mMap;
    private LocationClient mLocationClient;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(servicesConnected()) {
            mMap = getMap();
            mLocationClient = new LocationClient(getActivity(), this, this);
        } else {
            Log.d(LOG_TAG, getString(R.string.message_no_weather));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationClient.connect();
    }
    @Override
    public void onStop() {
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {


        setUpMapLocation();
    }

    @Override
    public void onDisconnected() {
        // do nothing
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {
                Log.e(LOG_TAG, "Error: ", e);
            }
        } else {
            // Get the error dialog from Google Play services
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), getActivity(),
                    CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
        }
    }

    private void setUpMapLocation() {
        Location currentLocation;
        LatLng coordinates;
        MarkerOptions customMarker;

        currentLocation = mLocationClient.getLastLocation();
        coordinates = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        customMarker = new MarkerOptions();
        customMarker.position(coordinates);

        mMap.addMarker(customMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, MAP_ZOOM));

    }

    private boolean servicesConnected() {
        // Check if Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        }

        // Get the error dialog from Google Play services
        GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
        return false;
    }
}
