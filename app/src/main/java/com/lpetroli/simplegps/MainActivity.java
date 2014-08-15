package com.lpetroli.simplegps;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private static final String LOG_TAG = "MainActivity";
    // Base URL for the OpenWeatherMap query
    private static final String URL_FORMAT =
            "http://api.openweathermap.org/data/2.5/weather?q=%s";

    private EditText mPlaceEditText;
    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
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

    /**
     * Search the weather of the place referenced by {@code R.id.edit_text_place}
     *
     * @param view a reference to current View
     */
    public void searchWeather(View view) {

        //Check whether there's internet connection available
        ConnectivityManager connMgr =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            // If no connection is available, warn user that operation cannot be performed
            String message = getString(R.string.message_no_weather);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            return;
        }

        /* 2014-08-15, from Lorenzo: I have a slight feeling that getting the same views
         * everytime the function is invoked is not a good thing. However, performing this
         * operation during OnCreate, trigger NullPointerException here. Suggestions??
         */
        mPlaceEditText = (EditText) findViewById(R.id.edit_text_place);
        mWeatherTextView = (TextView) findViewById(R.id.text_view_weather);

        String place = mPlaceEditText.getText().toString();
        Log.d(LOG_TAG, "Show weather from: " + place);

        new DownloadTask().execute(String.format(URL_FORMAT, place));

        mWeatherTextView.setText(place);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        private static final String LOG_TAG = "DownloadTask";

        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        protected String doInBackground(String... urls) {
            return downloadFromNetwork(urls[0]);
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(String result) {
            //mImageView.setImageBitmap(result);
        }

        private String downloadFromNetwork(String source) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String result = null;

            Log.d(LOG_TAG, "Downloading from: " + source);

            try {
                URL url = new URL(source);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    builder.append(line).append("\n");
                }

                if (builder.length() == 0) {
                    return null;
                }

                result = builder.toString();
                Log.d(LOG_TAG, result);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                result = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return result;
        }
    }
}
