package com.example.parkmania;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkmania.models.Parking;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ParkingDetails extends AppCompatActivity implements OnMapReadyCallback {

    TextView etBuildingCode, etLicence, etSuitNumber,etAddress,etDate,etNumberOfHours;
    private MapView mapView;
    private GoogleMap map;
    Button btnShowRoute;
    String addressText;
    Parking p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);

        Intent i = getIntent();
        p = (Parking) i.getSerializableExtra("PARKING");
        System.out.println(p.toString());

        etBuildingCode = findViewById(R.id.buildingCode);
        etLicence = findViewById(R.id.licence);
        etSuitNumber = findViewById(R.id.suitNumber);
        etAddress = findViewById(R.id.address);
        etDate = findViewById(R.id.date);
        etNumberOfHours = findViewById(R.id.numberOfHours);

        etBuildingCode.setText(p.getBuildingCode());
        etLicence.setText(p.getCarLicensePlateNumber());
        etSuitNumber.setText(p.getSuitNumber());
        etDate.setText(p.getDate());
        String nHrs = String.valueOf(p.getNoOfHrs()) + " Hrs";
        etNumberOfHours.setText(nHrs);
        useCurrentLocation(Double.parseDouble(p.getLatitude()), Double.parseDouble(p.getLongitude()));

        mapView = (MapView) findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }

    }


    public void actionBack(View view) {
        super.onBackPressed();
    }

    private void useCurrentLocation(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if(addresses != null && addresses.size() >= 1) {
                String country = addresses.get(0).getCountryName();
                String city = addresses.get(0).getLocality();
                String street = addresses.get(0).getThoroughfare();

                addressText = country + ", "+ city +", "+ street;
                etAddress.setText(addressText);

            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "UNKNOWN ERROR Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if(p != null) {
            MapsInitializer.initialize(getApplicationContext());
            //LatLng class is google provided class to get latiude and longitude of location.
            //GpsTracker is helper class to get the details for current location latitude and longitude.
            LatLng location = new LatLng(Double.parseDouble(p.getLatitude()), Double.parseDouble(p.getLongitude()));
            map = googleMap;
            map.addMarker(new MarkerOptions().position(location).title(addressText));
            map.moveCamera(CameraUpdateFactory.newLatLng(location));
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }
}