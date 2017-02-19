package com.example.cloudypedia.fawrysurveillanceapp.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudypedia.fawrysurveillanceapp.Activites.ReportActivity;
import com.example.cloudypedia.fawrysurveillanceapp.Activites.VistsActivity;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.GPSHandller;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Merchant;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Report;
import com.example.cloudypedia.fawrysurveillanceapp.R;
import com.example.cloudypedia.fawrysurveillanceapp.SyncService;
import com.example.cloudypedia.fawrysurveillanceapp.Utility;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

 public class Alert_Dialog extends DialogFragment {
    public String barcode="" , photo="" , range="" ;
    private TextView alerttxt ;
   public   ReportActivity reportActivity;
     ProgressDialog progressDialog;
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.alert_dialog, null);
        alerttxt = (TextView) view.findViewById(R.id.alert_dialog);

        String result="" ;
        if (!photo.isEmpty())
            result += photo + "\n";
        if(!barcode.isEmpty())
            result += barcode + "\n";
        if (!range.isEmpty())
            result += range + "\n" ;
        alerttxt.setText(result);



        builder.setTitle("انتبه").setView(view)
                // Add action buttons
                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Utility.uploadReport(reportActivity,
                                reportActivity.imgbitmap,
                                new Date().getTime(),
                                reportActivity.location.getLongitude(),
                                reportActivity.location.getLatitude(),
                                reportActivity.report.getName(),
                                reportActivity.report.getStatus() ,
                                reportActivity.report.getComment(),
                                reportActivity.jsonObject.toString());

                        progressDialog = ProgressDialog.show(reportActivity, "" ,"جارى  رفع الزيارة , انتظر من فضلك...", true);
                     ;

                    }
                })
                .setNegativeButton("رجوع", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
                    }
                });
        return builder.create();
    }

     @Override
     public void onStop()
     {super.onStop();
         progressDialog.dismiss();
     }
 }


