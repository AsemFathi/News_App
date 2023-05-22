package com.asem.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    LatLng latLng;
    GoogleMap googleMap;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        latLng = getIntent().getParcelableExtra("latlng");

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);




    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Move the camera to the LatLng and add a marker
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(new MarkerOptions().position(latLng));
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}