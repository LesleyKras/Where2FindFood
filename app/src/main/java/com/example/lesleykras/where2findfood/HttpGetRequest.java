package com.example.lesleykras.where2findfood;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

            //Open connection
            connection.connect();

            //Retrieve and parse data
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            String data;

            while ((data = reader.readLine()) != null){
                stringBuilder.append(data);
            }

            //Close connections
            reader.close();
            streamReader.close();

            result = stringBuilder.toString();

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
