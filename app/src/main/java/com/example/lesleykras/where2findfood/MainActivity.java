package com.example.lesleykras.where2findfood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private TextView status;
    private int counter;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchButton = (Button) findViewById(R.id.search);
        status = (TextView) findViewById(R.id.statusTextView);
        counter = 0;

        //Url endpoint
        String myUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.876689,4.466792&radius=100&type=restaurant&key=";

        // Save found locations from API call
        // TODO: Omzetten naar json ArrayList
        result = null;

        //make a new http request
        HttpGetRequest httpGetRequest = new HttpGetRequest();

        try {
            result = httpGetRequest.execute(myUrl).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter += 1;
                status.setText("Button is tapped " + counter + " times!");
                Log.d("=-LOG-=", "klik");

                if(result != null){
                    Log.d("=-LOG-=", result);
                }
            }
        };

        searchButton.setOnClickListener(onClickListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Toast.makeText(this, "Settings have been pressed", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
