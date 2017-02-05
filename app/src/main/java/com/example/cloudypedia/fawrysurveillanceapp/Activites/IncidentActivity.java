package com.example.cloudypedia.fawrysurveillanceapp.Activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.example.cloudypedia.fawrysurveillanceapp.Dialogs.Alert_Dialog;
import com.example.cloudypedia.fawrysurveillanceapp.R;
import com.example.cloudypedia.fawrysurveillanceapp.Utility;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
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
        spinner = (Spinner) findViewById(R.id.type_spinner);

        GPSHandller gpsHandller = new GPSHandller(this);
        location = gpsHandller.getLocation();

        spinner.setPromptId(R.string.incident_prompt);
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }

        });




      /*  save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try{
                  // JSONObject jsonObject = setJsonObject();

                   Utility.uploadReport(getApplicationContext(),imgbitmap,new Date().getTime(),location.getLongitude(),location.getLatitude(),report.getName().toString());

                   Toast.makeText(getApplicationContext()," Data Uploaded Successfully",Toast.LENGTH_SHORT).show();
                   onBackPressed();

               }
               catch(Exception ex)
               {
                   Toast.makeText(getApplicationContext(),"There is Error ",Toast.LENGTH_SHORT).show();
               }
            }
        })*/;
    }
/*    public JSONObject setJsonObject()
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
    }*/

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
 /*   public class ReportActivity extends AppCompatActivity {

        TextView gisLocation  , locationtxt, name ,merchantId ,salesId , salesName,salesEmail ,terminalID ,date ;
        Button range ,save,takephoto,BarcodeBtn;
        Report report;
        private  double Range;

        ImageView imageView;
        Bitmap imgbitmap;
        EditText comment ;
        final int CAMERA_REQUEST = 3;
        Location location;
        Spinner spinner ;
        String barcodetxt;
        String status;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.reportlayout);

            Intialize_view();
        }


        void Intialize_view()
        {
            Intialize_data();
            name = (TextView) findViewById(R.id.name_txt);
            name.setText("Name : " + report.getName());
            gisLocation = (TextView) findViewById(R.id.GISLocation_txt);
            gisLocation.setText( "GisLocation : "+report.getGISLocation());

            locationtxt = (TextView)  findViewById(R.id.location_txt);
            locationtxt.setText( "Location : "+report.getLocation());

            merchantId = (TextView)  findViewById(R.id.MerchantID_txt);
            merchantId.setText( "Merchant ID : "+report.getMerchantID());

            salesId =     (TextView)  findViewById(R.id.salesId_txt);
            salesId.setText( "Sales ID : "+report.getSalesID());

            salesName = (TextView)   findViewById(R.id.salesName_txt) ;
            salesName.setText( "Sales Name: "+report.getSalesName());

            salesEmail = (TextView) findViewById(R.id.salesEmail_txt) ;
            salesEmail.setText( "Sales Email : "+report.getSalesEmail());

            terminalID = (TextView) findViewById(R.id.TerminalID);
            terminalID.setText( "Barcode Value : "+ barcodetxt);

            date = (TextView) findViewById(R.id.date_txt);
            date.setText("Date : " + new Date().toString());

            range = (Button) findViewById(R.id.toggleButton);
            if(Range > 100.0)
            {
                range.setBackgroundColor(Color.RED);
            }
            range.setText( "Range : " + report.getRange()+ " meters");
   *//*     reportincdinet= (Button) findViewById(R.id.Report_btn);*//*

            imageView = (ImageView) findViewById(R.id.captured_img);
            save = (Button) findViewById(R.id.Save_Incident);
            takephoto = (Button) findViewById(R.id.photo_btn);
            comment = (EditText) findViewById(R.id.comment_txt);
            spinner = (Spinner) findViewById(R.id.type_spinner);


            spinner.setPromptId(R.string.incident_prompt);


            takephoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,CAMERA_REQUEST);
                }

            });


*//*        reportincdinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle ();
                b.putParcelable("report",report);
                Intent intent = new Intent(getApplicationContext(), IncidentActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });*//*

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imgbitmap == null || Range > 100.0 || barcodetxt.isEmpty()) {
                        Alert_Dialog alert_dialog = new Alert_Dialog();

                        if(imgbitmap== null)
                            alert_dialog.photo = "لا يوجد صورة" ;

                        if(Range > 100.0 )
                            alert_dialog.range = "المسافة بعيدة" ;

                        if(barcodetxt.isEmpty())
                            alert_dialog.barcode =   "لا يوجد باركود" ;

                        alert_dialog.show( getFragmentManager(), "تحذير");

                    }
                    else {
                        try {
                            JSONObject jsonObject = setJsonObject();

                            Utility.uploadReport(getApplicationContext(),
                                    imgbitmap,
                                    new Date().getTime(),
                                    location.getLongitude(),
                                    location.getLatitude(),
                                    report.getName().toString(),
                                    status,
                                    comment.getText().toString(),
                                    jsonObject.toString());

                            // Toast.makeText(getApplicationContext()," Data Uploaded Successfully",Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "There is Error ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            BarcodeBtn = (Button) findViewById(R.id.barcode_btn);
            BarcodeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent (getApplicationContext() , ScanBarcodeActivity.class);
                    startActivityForResult(intent,0);
                }
            });
        }

        @Override
        protected void onPause() {
            super.onPause();
            onBackPressed();
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(requestCode == 0 )
            {
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    if (data != null) {
                        Barcode barcode = data.getParcelableExtra("barcode");
                        barcodetxt =   barcode.displayValue;
                        terminalID.setText("Barcode Value : " + barcodetxt);
                    }
                    else {
                        barcodetxt ="No Barcode found ";
                        terminalID.setText(barcodetxt);
                    }
                }
            }
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

            else
            {
                super.onActivityResult(requestCode, resultCode, data);
            }

        }
        void Intialize_data()
        {
            report = new Report();
            GPSHandller gpsHandller = new GPSHandller(this);
            location = gpsHandller.getLocation();
            Bundle extras = getIntent().getExtras();
            Merchant current_merchant = extras.getParcelable("merchant");

            Range = extras.getDouble("distance");

            report .setName(current_merchant.getName());
            report.setLocation( Double.toString(current_merchant.getLatitude()) + " , "+ Double.toString(current_merchant.getLongitude()));
            report.setGISLocation(Double.toString(location.getLatitude()) + " , " + Double.toString(location.getLongitude()));
            report.setMerchantID(current_merchant.getTerminalID());
            report.setRange(Double.toString(Range) );
            report.setSalesID("123456");
            report.setSalesName(Utility.getPreferredName(this));
            report.setSalesEmail(Utility.getPreferredEmail(this));
            report.setTerminalID(barcodetxt);

        }

        public JSONObject setJsonObject()
        {
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("barcode", report.getTerminalID());
                jsonObject.put("range", report.getRange());
                jsonObject.put("incidentType" , spinner.getSelectedItem().toString());
                jsonObject.put("salesid" , Range);
                jsonObject.put("salesName" , Range);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return jsonObject;
        }
        @Override
        protected void onResume() {
            super.onResume();

        }
        public void checkStatus()
        {
            if(Range <= 100.0 && report.getMerchantID() == barcodetxt)
                status = "Succsessful";
            else if (Range > 100.0 )
                status = "UnSuccsessful (Range Exceed 100 Meter)";
            else if ( report.getMerchantID() != barcodetxt)
                status = "UnSuccsessful (Serial Doesn't Match)";
            else
                status = "UnSuccsessful";


        }
    }*/
}
