/*

package com.example.cloudypedia.fawrysurveillanceapp.DataFetcher;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.cloudypedia.fawrysurveillanceapp.Activites.VistsActivity;
import com.example.cloudypedia.fawrysurveillanceapp.AppConstants;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Merchant;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


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
import java.util.ArrayList;
import java.util.Date;


import android.os.AsyncTask;

import com.example.cloudypedia.fawrysurveillanceapp.Activites.VistsActivity;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Report;
import com.example.cloudypedia.fawrysurveillanceapp.Utility;

*/
/**
 * Created by dev3 on 2/5/2017.
 *//*


public class FetchVistsTask extends AsyncTask<String,String,Report[]> {

        private final String LOG_TAG = FetchVistsTask.class.getSimpleName();

         Context  context ;
        VistsActivity  vistsActivity;

    public FetchVistsTask(Context context,VistsActivity  vistsActivity) {
        this.context = context;
        this.vistsActivity = vistsActivity;
    }
        @Override
        protected void onPreExecute() {

          Utility.showProgressDialog( "جاري التحميل , انتظر من فضلك..."   ,context );
        }
        private Report[] getLocationDataFromJson(String ReportJsonStr)
                throws JSONException {


            JSONObject itemsJson = new JSONObject(ReportJsonStr);
            JSONArray jsonArray = itemsJson.getJSONArray("items");

            Report []reports = new Report[jsonArray.length()];

            for (int i = 0; i < reports.length  ; i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                Report r = new Report();
                r.setGISLocation(jsonObject.get("latitude").toString() +" , " + jsonObject.get("longitude").toString() );
                r.setName(jsonObject.get("user").toString());
                r.setReportId(jsonObject.get("id").toString());
                r.setReportDate(Long.parseLong(jsonObject.get("date").toString()));
                r.setSalesEmail(jsonObject.get("email").toString());
                r.setComment(jsonObject.get("comment").toString());
                r.setStatus(jsonObject.get("status").toString());
                r.setReportUrl(jsonObject.get("imageUrl").toString());

                if (!jsonObject.get("indexedCustomFields").equals(null)) {
                    JSONObject indexedCustomFields = (JSONObject) jsonObject.get("indexedCustomFields");
                    r.setBarcode(indexedCustomFields.get("barcode").toString());
                    r.setSalesID(indexedCustomFields.get("salesId").toString());
                    r.setRange(indexedCustomFields.get("range").toString());
                    r.setSalesName(indexedCustomFields.get("salesName").toString());
                    r.setIncidentType(indexedCustomFields.get("incidentType").toString());
                    if(indexedCustomFields.has("location"))
                    r.setLocation(indexedCustomFields.get("location").toString());
                    if(indexedCustomFields.has("terminalSerial"))
                        r.setTerminalSerial(indexedCustomFields.get("terminalSerial").toString());

                }
 */
/*          if (!jsonObject.get("unIndexedCustomFields").equals(null)) {
                JSONObject unindexedCustomFields = (JSONObject) jsonObject.get("unIndexedCustomFields");
                r.setAddress(unindexedCustomFields.get("StreeName").toString() + ", " +
                        unindexedCustomFields.get("AreaName").toString() );
                r.setPhone(unindexedCustomFields.get("MobileNum").toString());
                r.setMerchantType(unindexedCustomFields.get("MerchantTypeName").toString());
            }*//*

                reports[i] = r;

            }

            return reports;
        }
        @Override
        protected Report[] doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String ReportJsonStr = null;

            try {
                AppConstants.initSecuredConnection();

                String[] PARMETER_URL = null;

                Uri BuiltUri;

                PARMETER_URL = new String[2];
                PARMETER_URL[0] = "userIdToken";
                PARMETER_URL[1] = "date";
                //String encodedemail = Uri.encode(strings[1],"UTF-8");
                //   String encodedemail  = URLEncoder.encode(strings[1],"UTF-8");
                BuiltUri = Uri.parse(AppConstants.QUERY_ATTENDANCE_LOG).buildUpon().appendQueryParameter(PARMETER_URL[0], strings[0]).appendQueryParameter(PARMETER_URL[1], strings[1])
                        .build();


                URL url = new URL(BuiltUri.toString());
                Log.v("uri=", BuiltUri.toString());
                // Create the request to theMovieDb, and open the connectio
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }

                ReportJsonStr = buffer.toString();
                Log.v(LOG_TAG, "JSON String = " + ReportJsonStr);
            } catch (IOException e) {
                return null;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
            try {
                return getLocationDataFromJson(ReportJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Report[] reports) {

           vistsActivity.Reports = reports;
            Utility.dismissProgressDialog();
            if (reports != null) {
               vistsActivity.rowcontent = new ArrayList<>();
               vistsActivity.tableLayout.removeAllViews();
                for (int i = reports.length-1 ; i>=0 ; i--) {

                    Date d = new Date(reports[i].getReportDate());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm a");
                    if (reports[i].getName() != null)

                    {
                        Rowcontent rc = new Rowcontent(dateFormat.format(d), reports[i].getName().trim() , reports[i].getReportId());

                        rowcontent.add(rc);
                    }
                    // Toast.makeText(VistsActivity.this., r.getName(), Toast.LENGTH_SHORT).show();
                  //  Log.v(LOG_TAG, "JSON String = " + r.getName());

                }
                bulidTable();
            }
     else
            {
                Toast.makeText(VistsActivity.this,"خطأ في تحميل البيانات", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
