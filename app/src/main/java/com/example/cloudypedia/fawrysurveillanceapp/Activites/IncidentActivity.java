package com.example.cloudypedia.fawrysurveillanceapp.Activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudypedia.fawrysurveillanceapp.AppConstants;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.GPSHandller;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Merchant;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Report;
import com.example.cloudypedia.fawrysurveillanceapp.R;
import com.kosalgeek.android.photoutil.CameraPhoto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class IncidentActivity extends AppCompatActivity {

    Button save;
    Button takephoto;
    ImageView imageView;
    Bitmap imgbitmap;
    EditText comment ;
    final int CAMERA_REQUEST = 0;
    Location location;
    Spinner spinner ;
    Report report ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incident);

        Bundle extras = getIntent().getExtras();
         report = extras.getParcelable("report");
        imageView = (ImageView) findViewById(R.id.captured_img);
        save = (Button) findViewById(R.id.Save_Incident);
        takephoto = (Button) findViewById(R.id.photo_btn);
        comment = (EditText) findViewById(R.id.comment_txt);
        GPSHandller gpsHandller = new GPSHandller(this);
        location = gpsHandller.getLocation();
        spinner = (Spinner) findViewById(R.id.type_spinner);
        spinner.setPromptId(R.string.incident_prompt);
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }

        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try{
                  // JSONObject jsonObject = setJsonObject();
                   AppConstants.test(getApplicationContext(),imgbitmap,new Date().getTime(),location.getLongitude(),location.getLatitude(),report.getName().toString());

                   Toast.makeText(getApplicationContext()," Data Uploaded Successfully",Toast.LENGTH_SHORT).show();
                   onBackPressed();

               }
               catch(Exception ex)
               {
                   Toast.makeText(getApplicationContext(),"There is Error ",Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
    public JSONObject setJsonObject()
    {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("terminalId", report.getTerminalID());
            jsonObject.put("range", report.getRange());
            jsonObject.put("IncidentType" , spinner.getSelectedItem().toString());
            jsonObject.put("IncidentComment" , comment.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_REQUEST){

                try {
                    imgbitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(imgbitmap);

                        save.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }

            }



        }
    }
}
