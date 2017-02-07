package com.example.cloudypedia.fawrysurveillanceapp.DataFetcher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.cloudypedia.fawrysurveillanceapp.Activites.MapsActivity;
import com.example.cloudypedia.fawrysurveillanceapp.AppConstants;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.GPSHandller;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Merchant;
import com.example.cloudypedia.fawrysurveillanceapp.Fragments.MainFragment;
import com.example.cloudypedia.fawrysurveillanceapp.Utility;

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
import java.util.ArrayList;
import java.util.Arrays;


public class FetchLocationTask extends AsyncTask<String, String , Merchant[]> {

    private ProgressDialog progressDialog;
    Context context;
    double currentLat , currentLong;

    public FetchLocationTask(Context context, ProgressDialog progressDialog) {
        this.context = context;
        this.progressDialog = progressDialog;
        Log.e("Fawry", "in connection");
    }

    private final String LOG_TAG = FetchLocationTask.class.getSimpleName();

    private Merchant[] getLocationDataFromJson(String LocationJsonStr)
            throws JSONException {


        String result = LocationJsonStr;
        JSONArray jsonArray = new JSONArray(result);
        Merchant[] merchants = new Merchant[jsonArray.length()+1];

        merchants[0] = new Merchant(currentLat,currentLong,"My Location");

        for (int i = 1; i < merchants.length  ; i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i-1);

            Merchant m = new Merchant();
            m.setLatitude(Double.parseDouble(jsonObject.get("latitude").toString()));
            m.setLongitude(Double.parseDouble(jsonObject.get("longitude").toString()));
            m.setName(jsonObject.get("name").toString());

            if (!jsonObject.get("indexedCustomFields").equals(null)) {
                JSONObject indexedCustomFields = (JSONObject) jsonObject.get("indexedCustomFields");
                m.setTerminalID(indexedCustomFields.get("TerminalSerial").toString());
            }
            if (!jsonObject.get("unIndexedCustomFields").equals(null)) {
                JSONObject unindexedCustomFields = (JSONObject) jsonObject.get("unIndexedCustomFields");
                m.setAddress(unindexedCustomFields.get("StreeName").toString() + ", " +
                        unindexedCustomFields.get("AreaName").toString() );
                m.setPhone(unindexedCustomFields.get("MobileNum").toString());
                m.setMerchantType(unindexedCustomFields.get("MerchantTypeName").toString());
            }
            merchants[i] = m;

        }

        return merchants;
    }

    @Override
    protected Merchant[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String locationsJsonStr = null;

        final GPSHandller gpsHandller = new GPSHandller(context);
        Location location = gpsHandller.getLocation();

        if(location!=null) {
            currentLat = location.getLatitude();
            currentLong = location.getLongitude();

        }
        else {
            Utility.showMessage("خطأ في التواصل .. من فضلك حاول مرة اخري" ,context);
         /*   Toast.makeText(context, "خطأ في التواصل .. من فضلك حاول مرة اخري", Toast.LENGTH_SHORT).show();
*/
        }
        try {
            AppConstants.initSecuredConnection();

            final String BASE_URL = "https://4-dot-cloudypedia-abc.appspot.com/a/m/";
            String CONTROLLER_URL = null;
            String[] PARMETER_URL = null;
            Uri BuiltUri;
            if (params[0] == "terminalId") {
                CONTROLLER_URL = "getBranchbyTerminalId";
                PARMETER_URL = new String[1];
                PARMETER_URL[0] = "terminalId";
                BuiltUri = Uri.parse(BASE_URL + CONTROLLER_URL).buildUpon().appendQueryParameter(PARMETER_URL[0], params[1])
                        .build();
            } else {

                CONTROLLER_URL = "getBranchesbyNearest";
                PARMETER_URL = new String[2];
                PARMETER_URL[0] = "latitude";
                PARMETER_URL[1] = "longitude";


                BuiltUri = Uri.parse(BASE_URL + CONTROLLER_URL).buildUpon().appendQueryParameter(PARMETER_URL[0], params[1]).appendQueryParameter(PARMETER_URL[1], params[2])
                        .build();
            }
            URL url = new URL(BuiltUri.toString());
            Log.v("uri=", BuiltUri.toString());

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

            locationsJsonStr = buffer.toString();
            Log.v(LOG_TAG, "JSON String = " + locationsJsonStr);
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
            return getLocationDataFromJson(locationsJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    protected void onPostExecute(Merchant[] Merchants) {


        if (Merchants == null) {
                Toast.makeText(context, "Error in Fetching Data", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

        } else {
            if(Merchants.length == 0)
            {
                Toast.makeText(context, "خطأ في رقم الماكينة .. من فضلك تأكد من الرقم.", Toast.LENGTH_SHORT).show();
            }
            ArrayList<Merchant> merchants = new ArrayList<Merchant>(Arrays.asList(Merchants));

            Intent MapIntent = new Intent(context, MapsActivity.class);
            MapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle b = new Bundle();
            b.putParcelableArrayList("merchants", merchants);
            MapIntent.putExtras(b);
            context.startActivity(MapIntent);
            progressDialog.dismiss();
        }


    }
}