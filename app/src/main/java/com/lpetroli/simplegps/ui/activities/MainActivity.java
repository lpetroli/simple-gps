package com.lpetroli.simplegps.ui.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lpetroli.simplegps.R;
import com.lpetroli.simplegps.ui.fragments.MapsFragment;
import com.lpetroli.simplegps.ui.fragments.WeatherFragment;

public class MainActivity extends Activity {
    private static final String LOG_TAG = "MainActivity";
    private static final String TAB_SAVE_STATE_NAME = "TabIndex";

    private MapsFragment mMaps = new MapsFragment();
    private WeatherFragment mWeather = new WeatherFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();
        // Specify that tabs should be displayed in the action bar.
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            // Create a tab listener that is called when the user changes tabs.
            ActionBar.TabListener tabListener = new ActionBar.TabListener() {
                private void replaceTab(Fragment fragment, FragmentTransaction transaction) {
                    transaction.replace(R.id.container, fragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                }

                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                    if (tab.getText().equals(MapsFragment.MAPS_TAB_NAME)) {
                        replaceTab(mMaps, ft);
                    }
                    if (tab.getText().equals(WeatherFragment.WEATHER_TAB_NAME)) {
                        replaceTab(mWeather, ft);
                    }
                }

                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                    // ignore this event
                }

                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                    // ignore this event
                }
            };

            actionBar.addTab(actionBar.newTab().setText(MapsFragment.MAPS_TAB_NAME)
                    .setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText(WeatherFragment.WEATHER_TAB_NAME)
                    .setTabListener(tabListener));

            if(savedInstanceState != null) {
                int tabState = savedInstanceState.getInt(TAB_SAVE_STATE_NAME);
                actionBar.setSelectedNavigationItem(tabState);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        final ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            int i = actionBar.getSelectedNavigationIndex();
            outState.putInt(TAB_SAVE_STATE_NAME, i);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
