package com.example.branko.tester;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.branko.tester.utils.StatusBarChanger;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarChanger.changeColorForStatusBar(this);
        setContentView(R.layout.activity_map);
        getIntentData();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng sydney = new LatLng(mLatitude, mLongitude);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.setTrafficEnabled(true);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mLatitude = intent.getDoubleExtra(LATITUDE, 0);
        mLongitude = intent.getDoubleExtra(LONGITUDE,0);
    }

    public static Intent newIntent(Context context, double latitude, double longitude){
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(MapActivity.LATITUDE, latitude);
        intent.putExtra(MapActivity.LONGITUDE, longitude);
        return intent;
    }
}
