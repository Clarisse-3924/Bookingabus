package com.example.bookingabus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Map extends AppCompatActivity {
    public static final int REQUEST_CODE_LOCATION_SERVICE=1;
    TextView tvLat;
    TextView tvLong;
    TextView tvAddress;
    Button btnGetLoc;
    Button btnViewMap;
    LocationTrack locationTrack;
    Location location = new Location(LocationManager.GPS_PROVIDER);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        locationTrack = new LocationTrack(this);
        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLongt);
        tvAddress = findViewById(R.id.tvAddress);
        btnGetLoc = findViewById(R.id.btnGetLocation);
        btnViewMap = findViewById(R.id.btnViewMap);
        btnGetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//code to get Location
                if (ContextCompat.checkSelfPermission(Map.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Map.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},

                            REQUEST_CODE_LOCATION_SERVICE);
                } else {
                    location = locationTrack.fetchLocation();
                    double lat = location.getLatitude();
                    double longt = location.getLongitude();
                    tvLat.setText("" + lat);
                    tvLong.setText("" + longt);
                    String address = locationTrack.getAddress(lat, longt);
                    tvAddress.setText(address);
                }
            }
        });
        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//code to open the address on the map
                Intent intent = new Intent(Map.this, MapsActivity.class);
                double lat = location.getLatitude();
                double lnt = location.getLongitude();
                intent.putExtra("LAT", lat);
                intent.putExtra("LNT", lnt);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_CODE_LOCATION_SERVICE &&
                grantResults[0]==PackageManager.PERMISSION_GRANTED){
            location =locationTrack.fetchLocation();
        }
        else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}