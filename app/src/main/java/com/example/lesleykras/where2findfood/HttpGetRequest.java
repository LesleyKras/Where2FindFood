package com.example.lesleykras.where2findfood;


import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetRequest extends AsyncTask <String, Void, String>{

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected String doInBackground(String... strings) {
        String stringUrl = strings[0];
        String result;

        try{
            //Create URL
            URL httpUrl = new URL(stringUrl);

            //create a connection
            HttpURLConnection connection = (HttpURLConnection)
                    httpUrl.openConnection();

            //Set REQUEST settings
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(true);
            HttpURLConnection.setFollowRedirects(true);

            //Open connection
            connection.connect();

            //Retrieve and parse data
            InputStream inputStream = connection.getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            if (bytes.length == 0) {
                // TODO: Foutafhandeling
                Log.d("=-LOG-=", "No data received");
            }
            // TODO: JSON verwerken
            result = new String(bytes);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
