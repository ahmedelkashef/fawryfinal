package com.example.cloudypedia.fawrysurveillanceapp.Activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewReportActivity extends AppCompatActivity {

    public TextView gisLocation, locationtxt, name, merchantId, salesId, salesName, salesEmail, barcode, date
            , comment ,type ,terminalId  , address,status;

    public Report report;
    public  Button range , showImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_report);
        Utility.setActionBar(getSupportActionBar(),this,"home");
        Intialize_view();

    }
    void Intialize_view() {
        Intialize_data();

        name = (TextView) findViewById(R.id.name_txt);
        name.setText("اسم التاجر : " + report.getName().trim());

        gisLocation = (TextView) findViewById(R.id.GISLocation_txt);
        gisLocation.setText("موقعك الحالي : " + report.getGISLocation());

        locationtxt = (TextView) findViewById(R.id.location_txt);
        locationtxt.setText("موقع التاجر : " + report.getLocation());

        merchantId = (TextView) findViewById(R.id.MerchantID_txt);
        merchantId.setText("الباركود : " + report.getTerminalSerial());

      /*  salesId = (TextView) findViewById(R.id.salesId_txt);
        salesId.setText("رقم المندوب : " + report.getSalesID());*/

        salesName = (TextView) findViewById(R.id.salesName_txt);
        salesName.setText("اسم المندوب : " + report.getSalesName());

        salesEmail = (TextView) findViewById(R.id.salesEmail_txt);
        salesEmail.setText("بريد المندوب : " + report.getSalesEmail());

        barcode = (TextView) findViewById(R.id.barcode_txt);
        barcode.setText("قيمة الباركود : " + report.getBarcode());

        terminalId = (TextView) findViewById(R.id.TerminalID);
        terminalId.setText(" رقم التاجر : " + report.getTerminalId());

        address = (TextView) findViewById(R.id.address_txt);
        address.setText("العنوان : " + report.getAddress());

        date = (TextView) findViewById(R.id.date_txt);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm a");//EEEE, MMMM dd, yyyy

        Date d = new Date(report.getReportDate());
        date.setText("التاريخ : " + dateFormat.format(d));

        range = (Button) findViewById(R.id.toggleButton);

        if (Double.parseDouble(report.getRange()) > 100.0) {
            range.setBackgroundColor(Color.RED);
        }

        range.setText("المسافة : " + report.getRange() + " متر");
        showImage = (Button) findViewById(R.id.show_photo_btn);


        status = (TextView) findViewById(R.id.status_txt);
        status.setText("حالة الزيارة :" + report.getStatus());

        type = (TextView)findViewById(R.id.type_txt);
        type.setText("نوع الزيارة : " + report.getIncidentType());

        comment = (TextView) findViewById(R.id.comment_txt);
        comment.setText("تعليق : " +report.getComment());

        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewReportActivity.this , PhotoActivity.class);
                intent.putExtra("photoUrl",report.getReportUrl());
                startActivity(intent);
            }
        });

    }
    void Intialize_data() {

        Bundle extras = getIntent().getExtras();
        report = extras.getParcelable("reportKey");

    }
}
