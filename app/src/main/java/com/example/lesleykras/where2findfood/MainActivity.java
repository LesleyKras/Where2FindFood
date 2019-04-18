package com.example.lesleykras.where2findfood;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Button searchButton;
    private Button listViewButton;
    private MapView mapView;
    private GoogleMap gmap;
    private String API_KEY;
    private String ApiUrl = "";
    private LatLng latLong = new LatLng(51.876689, 4.466792);

    private String result;
    private JSONObject jsonResult;

    private final static String L_TAG = "-=LOG=-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchButton = (Button) findViewById(R.id.search);
        listViewButton = (Button) findViewById(R.id.listView);
        listViewButton.setEnabled(false);

        //Get API_Key in a safe way from manifest
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            API_KEY = bundle.getString("com.google.android.geo.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(L_TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(L_TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        //Url bouwen
        buildAPIUrl();

        mapView = findViewById(R.id.mapView);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(API_KEY);
        }

        //Map initialiseren
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);


        //make a new http request
        HttpGetRequest httpGetRequest = new HttpGetRequest();

        try {
            result = httpGetRequest.execute(this.ApiUrl).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try {
            jsonResult = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        View.OnClickListener getApiResults = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(L_TAG, "klik op get Food");

                if(result != null){
                    try {
                        Log.d(L_TAG, jsonResult.getJSONArray("results").getJSONObject(0).toString(10));
                    } catch (Exception e) {
                        Log.d(L_TAG, "Error; Geen data ontvangen");
                    }
                }

//              LatLng markerLocation = new LatLng(jsonResult.getDouble("lat"),jsonResult.getDouble("long"));


                gmap.addMarker(new MarkerOptions().position(latLong).title("Your position"));
                gmap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
                listViewButton.setEnabled(true);

                try {
                    Log.d(L_TAG + "JsonObject: ", ":  :" + jsonResult.getJSONArray("results").get(0).toString());
                } catch (JSONException e) {
                    Log.d(L_TAG, "Error: " + e);
                }

            }
        };

        View.OnClickListener goToListView = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(L_TAG, "klik op ListView");
            }
        };

        searchButton.setOnClickListener(getApiResults);
        listViewButton.setOnClickListener(goToListView);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);

        Log.d(L_TAG, "Map initialised");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void buildAPIUrl(){
        this.ApiUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.876689,4.466792&radius=100&type=restaurant&key="+API_KEY;
    }
}
