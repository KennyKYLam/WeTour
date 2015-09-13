package com.example.kenny.mhack6tripapp;
import java.io.*;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.List;

import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    String city = "toronto";
    double lat;
    double longi;

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextView txtView = (TextView)findViewById(R.id.ptofinterest);

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://terminal2.expedia.com/x/geo/features?within=5km&lng=\" + longi + \"&lat=\" + lat + \"&type=point_of_interest&apikey=6jAl4srwX4rJStZh6Iq2HMf5OUsQKvCO");

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                txtView.append(current);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace(); //If you want further info on failure...
            }
        }

    }
    public void onMapReady(GoogleMap map) {

        try {
            Geocoder gc = new Geocoder(this);
            List<Address> list = gc.getFromLocationName(city, 1);
            Address add = list.get(0);
            String locality = add.getLocality();
            lat = add.getLatitude();
            longi = add.getLongitude();

            LatLng citylocation = new LatLng(lat, longi);

            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(citylocation, 13));
            String str = city;
            String city = str.substring(0, 1).toUpperCase() + str.substring(1);
            map.addMarker(new MarkerOptions()
                    .title(city)
                    .snippet(city + " Trip!")
                    .position(citylocation));
        } catch(IOException e) {
            System.out.println("Completed!");
        }

    }
}