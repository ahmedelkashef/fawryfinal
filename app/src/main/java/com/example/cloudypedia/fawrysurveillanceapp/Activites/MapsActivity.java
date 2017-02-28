package com.example.cloudypedia.fawrysurveillanceapp.Activites;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cloudypedia.fawrysurveillanceapp.Classes.GPSHandller;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Merchant;
import com.example.cloudypedia.fawrysurveillanceapp.Controller;
import com.example.cloudypedia.fawrysurveillanceapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
   Location location;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    LatLng currentLocation;
    Marker marker ;
    ImageButton homebtn, refreshbtn;
   // Marker pressedmarker;
    ProgressDialog progressDialog;
    Marker currentMarker;

    private ArrayList<Merchant>  merchants;

    GPSHandller gpsHandller;
    protected String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
    Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int REQUEST_CODE_PERMISSION = 4;

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }
    public void onSaveInstanceState(Bundle outState) {
        outState.putDouble("pressedLat", currentMarker.getPosition().latitude);
        outState.putDouble("pressedLong", currentMarker.getPosition().longitude);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Bundle extras = getIntent().getExtras();
        merchants = extras.getParcelableArrayList("merchants");
        if(savedInstanceState == null)
        {
            currentLocation = new LatLng(merchants.get(0).getLatitude(), merchants.get(0).getLongitude());
        }
        else{
            currentLocation  = new LatLng(savedInstanceState.getDouble("pressedLat"), savedInstanceState.getDouble("pressedLong")) ;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);
        setContentView(R.layout.activity_maps);

        homebtn = (ImageButton) findViewById(R.id.homeButton);
        refreshbtn = (ImageButton) findViewById(R.id.Refresh);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(MapsActivity.this, "" ,"جارى التحميل, انتظر من فضلك...", true);

                Controller controller = new Controller(MapsActivity.this, progressDialog);
                controller.getBranchesByNearest(Double.toString(currentMarker.getPosition().latitude),Double.toString(currentMarker.getPosition().longitude));

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
        }

        mMap.setMyLocationEnabled(true);

             LatLngBounds.Builder builder = new LatLngBounds.Builder();
             currentMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
             currentMarker.showInfoWindow();

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getBaseContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getBaseContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getBaseContext());
                snippet.setTextColor(Color.BLACK);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        for (int i = 1 ; i<merchants.size() ; i++)
        {
            String sinippet = "Terminal ID : " + merchants.get(i).getTerminalID() +"\n"+"Address: "+ merchants.get(i).getAddress();
            marker =  mMap.addMarker(new MarkerOptions().position(new LatLng(merchants.get(i).getLatitude(), merchants.get(i).getLongitude()))
                    .title(merchants.get(i).getName()).snippet(sinippet));

            builder.include(marker.getPosition());
            marker.setTag(merchants.get(i));
        }

        builder.include(currentMarker.getPosition());
        final   LatLngBounds bounds = builder.build();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
            }
        });


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(!(marker.equals(currentMarker))) {

                    float []result = new float[1];

                    Location.distanceBetween(location.getLatitude(),location.getLongitude(), marker.getPosition().latitude,marker.getPosition().longitude, result);


                    double distance = Math.round (result[0] * 100.0) / 100.0;  ;
                    Merchant currentMerchant = (Merchant) marker.getTag();

                    Bundle b = new Bundle ();
                    b.putParcelable("merchant",currentMerchant);

                    Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                    intent.putExtra("distance",distance);

                    intent.putExtra("currentLocation", location);
                   /* intent.putExtra("currentlatitude",currentLocation.latitude);
                    intent.putExtra("currentlongtitude" , currentLocation.longitude );*/
                    intent.putExtras(b);
                    startActivity(intent);
                }

            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                 currentMarker.remove();
                 currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(" My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            refreshbtn.setVisibility(View.VISIBLE);
            }
        });

        }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            this.location = location;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }


    /**
     * Created by dev3 on 2/5/2017.
     */

}

