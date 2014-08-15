package com.lpetroli.simplegps.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lpetroli.simplegps.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class WeatherFragment extends Fragment {
    private static final String LOG_TAG = "WeatherFragment";

    // Base URL for the OpenWeatherMap query
    private static final String URL_FORMAT =
            "http://api.openweathermap.org/data/2.5/weather?q=%s";

    private EditText mPlaceEditText;
    private TextView mWeatherTextView;
    private Button mSearchButton;

    public WeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        mPlaceEditText = (EditText) rootView.findViewById(R.id.edit_text_place);
        mWeatherTextView = (TextView) rootView.findViewById(R.id.text_view_weather);
        mSearchButton = (Button) rootView.findViewById(R.id.button_search);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWeather(view);
            }
        });

        return rootView;
    }

    /**
     * Search the weather of the place referenced by {@code R.id.edit_text_place}
     *
     * @param view a reference to current View
     */
    public void searchWeather(View view) {
        //Check whether there's internet connection available
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            // If no connection is available, warn user that operation cannot be performed
            String message = getString(R.string.message_no_weather);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            return;
        }

        String place = mPlaceEditText.getText().toString();
        Log.d(LOG_TAG, "Show weather from: " + place);

        new DownloadTask().execute(String.format(URL_FORMAT, place));
    }

    void setDownloadResult(String result) {
        if(result == null) {
            // Warn user that operation cannot be performed
            String message = getString(R.string.message_no_weather);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            return;
        }

        mWeatherTextView.setText(result);
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
            Log.d(LOG_TAG, result);
            setDownloadResult(result);
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
