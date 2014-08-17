package com.lpetroli.simplegps.ui.fragments;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.lpetroli.simplegps.R;

public class MapsFragment extends MapFragment {
    private static final String LOG_TAG = "MapsFragment";

    private static final int MAX_GET_MAP_RETRIES = 5;
    private static final long WAIT_MILLS = 500;

    private GoogleMap mMap;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* This background task is intended to acquire a valid GoogleMap reference, taking into
         * account delays that may exist on map availability during fragment creation
         */
        Runnable background = new Runnable() {
            @Override
            public void run() {
                MapFragment mFragment = MapFragment.newInstance();
                for(int retries = 0; retries < MAX_GET_MAP_RETRIES; retries++){
                    if((mMap = mFragment.getMap()) != null) {
                        break;
                    }
                    try {
                        Thread.sleep(WAIT_MILLS, 0);
                    } catch (InterruptedException e) {
                        Log.e(LOG_TAG, "Error: ", e);
                    }
                }

                if(mMap == null) {
                    Log.d(LOG_TAG, getString(R.string.message_no_weather));
                }
            }
        };

        new Thread( background ).start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
