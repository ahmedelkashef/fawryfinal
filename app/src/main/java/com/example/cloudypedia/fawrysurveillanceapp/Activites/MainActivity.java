package com.example.cloudypedia.fawrysurveillanceapp.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.cloudypedia.fawrysurveillanceapp.DataFetcher.FetchLocationTask;
import com.example.cloudypedia.fawrysurveillanceapp.Fragments.MainFragment;
import com.example.cloudypedia.fawrysurveillanceapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {
    protected GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction()
                .add(android.R.id.content, new MainFragment()).commit();
    }

}
