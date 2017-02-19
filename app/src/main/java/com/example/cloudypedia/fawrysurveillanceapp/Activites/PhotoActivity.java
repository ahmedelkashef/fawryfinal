package com.example.cloudypedia.fawrysurveillanceapp.Activites;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cloudypedia.fawrysurveillanceapp.R;
import com.example.cloudypedia.fawrysurveillanceapp.Utility;
import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    ImageView photoimageview;
    TextView nophototxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Utility.setActionBar(getSupportActionBar(),getApplicationContext());
        photoimageview = (ImageView) findViewById(R.id.photoimageView);
        nophototxt = (TextView) findViewById(R.id.phototxt);

        Bundle extras = getIntent().getExtras();
        String photoUrl = extras.getString("photoUrl");

        if(!photoUrl.equals("null")) {
            Picasso.with(this).load(photoUrl).into(photoimageview);
            nophototxt.setVisibility(View.INVISIBLE);
        }
        else
        {
            nophototxt.setVisibility(View.VISIBLE);
            //nophototxt.setText("لا يوجد صورة");
        }

    }
}
