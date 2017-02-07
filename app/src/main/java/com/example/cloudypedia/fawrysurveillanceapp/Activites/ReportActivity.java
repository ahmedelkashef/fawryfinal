package com.example.cloudypedia.fawrysurveillanceapp.Activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudypedia.fawrysurveillanceapp.Classes.GPSHandller;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Merchant;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Report;
import com.example.cloudypedia.fawrysurveillanceapp.Dialogs.Alert_Dialog;
import com.example.cloudypedia.fawrysurveillanceapp.R;
import com.example.cloudypedia.fawrysurveillanceapp.Utility;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public  class ReportActivity extends AppCompatActivity {

  public   TextView gisLocation, locationtxt, name, merchantId, salesId, salesName, salesEmail, terminalID, date;
    public  Button range, BarcodeBtn, save, takephoto;
    public   Report report;
    public double Range;
    public   Location location;
    public String barcodetxt ;
    private ImageView imageView;
    public Bitmap imgbitmap;
    public EditText comment;
    private final int CAMERA_REQUEST = 3;
    public Spinner spinner;
    public JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportlayout);

        if (savedInstanceState == null) {
            barcodetxt = "";

        } else {
            barcodetxt = savedInstanceState.getString("barcodetxt");
        }
        Intialize_view();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("barcodetxt", barcodetxt);
        super.onSaveInstanceState(outState);
    }

    void Intialize_view() {
        Intialize_data();
        name = (TextView) findViewById(R.id.name_txt);
        name.setText("اسم الموقع : " + report.getName());
        gisLocation = (TextView) findViewById(R.id.GISLocation_txt);
        gisLocation.setText("موقعك الحالي : " + report.getGISLocation());

        locationtxt = (TextView) findViewById(R.id.location_txt);
        locationtxt.setText("موقع التاجر : " + report.getLocation());

        merchantId = (TextView) findViewById(R.id.MerchantID_txt);
        merchantId.setText("رقم التاجر : " + report.getTerminalSerial());

        salesId = (TextView) findViewById(R.id.salesId_txt);
        salesId.setText("رقم المندوب : " + report.getSalesID());

        salesName = (TextView) findViewById(R.id.salesName_txt);
        salesName.setText("اسم المندوب : " + report.getSalesName());

        salesEmail = (TextView) findViewById(R.id.salesEmail_txt);
        salesEmail.setText("بريد المندوب : " + report.getSalesEmail());

        terminalID = (TextView) findViewById(R.id.TerminalID);
        terminalID.setText(" قيمة الباركود : " + barcodetxt);

        date = (TextView) findViewById(R.id.date_txt);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm a");//EEEE, MMMM dd, yyyy

        Date date1 = new Date();
        date.setText("التاريخ : " + dateFormat.format(date1));

        range = (Button) findViewById(R.id.toggleButton);
        if (Range > 100.0) {
            range.setBackgroundColor(Color.RED);
        }
        range.setText("المسافة : " + report.getRange() + " متر");


        imageView = (ImageView) findViewById(R.id.captured_img);
        save = (Button) findViewById(R.id.Save_Incident);
        takephoto = (Button) findViewById(R.id.photo_btn);
        comment = (EditText) findViewById(R.id.comment_txt);
        spinner = (Spinner) findViewById(R.id.type_spinner);

        report.setComment(comment.getText().toString());
        BarcodeBtn = (Button) findViewById(R.id.barcode_btn);
        BarcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScanBarcodeActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 3);
            }

        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imgbitmap == null || barcodetxt == null || Range > 100.0) {

                    Alert_Dialog newDialog = new Alert_Dialog();

                    if (imgbitmap == null) {
                        newDialog.photo = "لا يوجد صورة";
                    }
                    if( !report.getTerminalSerial().equals( barcodetxt))
                    {
                        if (barcodetxt.isEmpty() || barcodetxt == "لا يوجد بار كود") {
                            newDialog.barcode = "لا يوجد  باركود";
                        }
                        else
                        {
                            newDialog.barcode = "الباركود غير متطابق";
                        }
                    }


                    if (Range > 100) {

                        newDialog.range = "المسافة بعيدة";
                    }
                    jsonObject = setJsonObject();
                    newDialog.reportActivity = ReportActivity.this;
                    newDialog.show(getFragmentManager(), "انذار");
                } else {
                    checkStatus();
                    try {
                        report.setComment( comment.getText().toString());
                        jsonObject = setJsonObject();
                        Utility.uploadReport(getApplicationContext(),
                                imgbitmap,
                                new Date().getTime(),
                                location.getLongitude(),
                                location.getLatitude(),
                                report.getName(),
                                report.getStatus() ,
                                report.getComment() ,
                                jsonObject.toString());

                        //Toast.makeText(getApplicationContext(), " Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent( ReportActivity.this , VistsActivity.class);
                        startActivity(intent);
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "There is Error ", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        //onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    barcodetxt = barcode.displayValue;
                    terminalID.setText("قيمة الباركود : " + barcodetxt);
                } else {
                    barcodetxt = "لا يوجد بار كود";
                    terminalID.setText(barcodetxt);
                }
            }

        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {


                try {
                    imgbitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(imgbitmap);

                    save.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }

            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    void Intialize_data() {

        report = new Report();

        GPSHandller gpsHandller = new GPSHandller(this);
        location = gpsHandller.getLocation();

        Bundle extras = getIntent().getExtras();
        Merchant current_merchant = extras.getParcelable("merchant");

        // sharedpreferences = getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);

        //  report = extras.getParcelable("sales");

        Range = extras.getDouble("distance");

        report.setName(current_merchant.getName());
        report.setLocation(Double.toString(current_merchant.getLatitude()) + " , " + Double.toString(current_merchant.getLongitude()));

        if (location != null)
            report.setGISLocation(Double.toString(location.getLatitude()) + " , " + Double.toString(location.getLongitude()));
        else
            Toast.makeText(getApplicationContext(), "خطأ في التواصل .. من فضلك حاول مرة اخري", Toast.LENGTH_SHORT).show();

        report.setTerminalSerial(current_merchant.getTerminalID());
        report.setRange(Double.toString(Range) );

    /*        report.setSalesID(sharedpreferences.getString(AppConstants.Id,""));
            report.setSalesName(sharedpreferences.getString(AppConstants.Name,""));
            report.setSalesEmail(sharedpreferences.getString(AppConstants.Email,""));*/

        report.setSalesID("12345");
        report.setSalesName(Utility.getPreferredName(this));
        report.setSalesEmail(Utility.getPreferredEmail(this));
        //report.setTerminalID();
        report.setStatus( "غير ناجحة");


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public JSONObject setJsonObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("range", report.getRange());
            jsonObject.put("incidentType", spinner.getSelectedItem().toString());
            jsonObject.put("barcode", barcodetxt);
            jsonObject.put("salesName", report.getSalesName());
            jsonObject.put("salesId",report.getSalesID());
            jsonObject.put("location" , report.getLocation());
            jsonObject.put("terminalSerial" , report.getTerminalSerial());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }


    public void checkStatus()
    {
        if(Range <= 100.0 && report.getTerminalSerial() == barcodetxt)
            report.setStatus("ناجحة") ;
        else if (Range > 100.0 )
            report.setStatus("غير ناجحة (المسافة تعدت 100 متر)");
        else if ( report.getTerminalSerial() != barcodetxt)
            report.setStatus("غير ناجحة (الباركود غير مطابق)");
        else
            report.setStatus("غير ناجحة");


    }





    // -----------------------------------------------------------------------------------------------------------------------------------------------------------//


}