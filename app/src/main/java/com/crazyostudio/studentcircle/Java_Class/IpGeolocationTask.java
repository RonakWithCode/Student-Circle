package com.crazyostudio.studentcircle.Java_Class;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IpGeolocationTask extends AsyncTask<String, Void, String> {

    private static final String API_KEY = "3936bf9a40da22e787a370aecf4908cd"; // Replace with your ipstack API key
    private static final String API_URL = "http://api.ipstack.com/";

    private IpGeolocationListener listener;

    public IpGeolocationTask(IpGeolocationListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String ipAddress = params[0];
        String urlString = API_URL + ipAddress + "?access_key=" + API_KEY;

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            Log.e("IpGeolocationTask", "Error fetching data", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);
                String city = jsonResult.optString("city");
                String country = jsonResult.optString("country_name");

                if (listener != null) {
                    listener.onIpGeolocationResult(city, country);
                }
            } catch (JSONException e) {
                Log.e("IpGeolocationTask", "Error parsing JSON", e);
            }
        }
    }

    public interface IpGeolocationListener {
        void onIpGeolocationResult(String city, String country);
    }
}
