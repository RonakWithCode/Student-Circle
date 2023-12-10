package com.crazyostudio.studentcircle.Java_Class;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetPublicIpAddressTask extends AsyncTask<Void, Void, String> {

    private static final String IP_SERVICE_URL = "https://api.ipify.org?format=json";

    private IpAddressListener listener;

    public GetPublicIpAddressTask(IpAddressListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url = new URL(IP_SERVICE_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
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
            Log.e("GetPublicIpAddressTask", "Error fetching data", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            try {
                // Parse JSON to get the IP address
                String ipAddress = new JSONObject(result).optString("ip");

                if (listener != null) {
                    listener.onIpAddressResult(ipAddress);
                }
            } catch (JSONException e) {
                Log.e("GetPublicIpAddressTask", "Error parsing JSON", e);
            }
        }
    }

    public interface IpAddressListener {
        void onIpAddressResult(String ipAddress);
    }
}
