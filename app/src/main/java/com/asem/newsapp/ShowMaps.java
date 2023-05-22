package com.asem.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

public class ShowMaps extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_maps);

        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout , fragment).commit();

    }
}