package com.example.cloudypedia.fawrysurveillanceapp.Activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.example.cloudypedia.fawrysurveillanceapp.Classes.GPSHandller;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Merchant;
import com.example.cloudypedia.fawrysurveillanceapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double longitude, latitude;
    Marker marker ;

   private ArrayList<Merchant>  merchants;

    GPSHandller gpsHandller;
    protected String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
    Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int REQUEST_CODE_PERMISSION = 4;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Bundle extras = getIntent().getExtras();
        merchants = extras.getParcelableArrayList("merchants");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
        }
        mMap.setMyLocationEnabled(true);

        gpsHandller = new GPSHandller(this);
        Location location = gpsHandller.getLocation();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        final Marker currentMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("My location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        currentMarker.showInfoWindow();
        for (int i = 0 ; i<merchants.size() ; i++)
        {
            marker =  mMap.addMarker(new MarkerOptions().position(new LatLng(merchants.get(i).getLatitude(), merchants.get(i).getLongitude())).title(merchants.get(i).getName()));
            builder.include(marker.getPosition());
            marker.setTag(merchants.get(i));
        }
        builder.include(currentMarker.getPosition());
        final   LatLngBounds bounds = builder.build();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        });


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(!(marker.equals(currentMarker))) {
                    float []result = new float[1];
                    Location.distanceBetween(currentMarker.getPosition().latitude,currentMarker.getPosition().longitude, marker.getPosition().latitude,marker.getPosition().longitude, result);

                    double distance = Math.round (result[0] * 100.0) / 100.0;  ;
                    Merchant currentMerchant = (Merchant) marker.getTag();

                    Bundle b = new Bundle ();
                    b.putParcelable("merchant",currentMerchant);

                    Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                    intent.putExtra("distance",distance);
                    intent.putExtras(b);
                    startActivity(intent);
                }

            }
        });
        }


    }

