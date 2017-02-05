package com.example.cloudypedia.fawrysurveillanceapp.DataFetcher;

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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dev3 on 2/5/2017.
 */

public class FetchVistsTask extends AsyncTask<String,String,Report[]>{

    private final String LOG_TAG = FetchVistsTask.class.getSimpleName();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    private Report[] getLocationDataFromJson(String ReportJsonStr)
            throws JSONException {



        String result = ReportJsonStr;
        JSONArray jsonArray = new JSONArray(result);
        Report [] reports = new Report[jsonArray.length()];


        for (int i = 0; i < reports.length  ; i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            Report r = new Report();
            r.setGISLocation(jsonObject.get("latitude").toString()  + jsonObject.get("longitude").toString() );
            r.setName(jsonObject.get("user").toString());
            r.setReportId(jsonObject.get("id").toString());
            r.setReportDate(Long.parseLong(jsonObject.get("date").toString()));
            r.setSalesEmail(jsonObject.get("email").toString());
            r.setComment(jsonObject.get("comment").toString());

            if (!jsonObject.get("indexedCustomFields").equals(null)) {
                JSONObject indexedCustomFields = (JSONObject) jsonObject.get("indexedCustomFields");
                r.setTerminalID(indexedCustomFields.get("barcode").toString());
                r.setSalesID(indexedCustomFields.get("barcode").toString());
            }
          /*  if (!jsonObject.get("unIndexedCustomFields").equals(null)) {
                JSONObject unindexedCustomFields = (JSONObject) jsonObject.get("unIndexedCustomFields");
                r.setAddress(unindexedCustomFields.get("StreeName").toString() + ", " +
                        unindexedCustomFields.get("AreaName").toString() );
                r.setPhone(unindexedCustomFields.get("MobileNum").toString());
                r.setMerchantType(unindexedCustomFields.get("MerchantTypeName").toString());
            }*/
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

            final String BASE_URL = "https://4-dot-cloudypedia-abc.appspot.com/a/checkin";
            String CONTROLLER_URL = null;
            String[] PARMETER_URL = null;
            Uri BuiltUri;
                CONTROLLER_URL = "getBranchbyTerminalId";
                PARMETER_URL = new String[1];
                PARMETER_URL[0] = "date";
                PARMETER_URL[1] = "email";
                BuiltUri = Uri.parse(BASE_URL + CONTROLLER_URL).buildUpon().appendQueryParameter(PARMETER_URL[0], strings[0]).appendQueryParameter(PARMETER_URL[1], strings[1])
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

        for (Report r : reports
             ) {
           // Toast.makeText(VistsActivity.this., r.getName(), Toast.LENGTH_SHORT).show();
            Log.v(LOG_TAG, "JSON String = " + r.getName());

        }
        super.onPostExecute(reports);
    }
}
