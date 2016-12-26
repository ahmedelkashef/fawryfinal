package com.example.cloudypedia.fawrysurveillanceapp.DataFetcher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.cloudypedia.fawrysurveillanceapp.Activites.MapsActivity;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Merchant;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dev3 on 11/24/2016.
 */

public class FetchLocationTask extends AsyncTask<String, Void, Merchant[]> {

    private ProgressDialog progressDialog;
    String serviceType;
    Context context;
    public FetchLocationTask(Context context ,ProgressDialog progressDialog) {
        this.context = context;
        this.progressDialog = progressDialog;
        Log.e("Fawry", "in connection");
    }
    private final String LOG_TAG = FetchLocationTask.class.getSimpleName();

    private Merchant[] getLocationDataFromJson(String LocationJsonStr)
            throws JSONException {

//        String Long = "longitude" ;
//        String Lat = "latitude" ;
//
//
//        JSONArray LocationArray = new JSONArray(LocationJsonStr);
//        LatLng[] latLngs = new LatLng[ LocationArray.length()];
//
//        for (int  i = 0 ;  i < LocationArray.length(); i++ )
//        {
//            JSONObject SingleJson = LocationArray.getJSONObject(i);
//            LatLng latlng = new LatLng(SingleJson.getDouble(Long),SingleJson.getDouble(Lat) );
////            LatLng latlng = new LatLng(SingleJson.getDouble(Lat),SingleJson.getDouble(Long) );
//            latLngs[i] = latlng ;
//        }
//        return latLngs;

        String result = LocationJsonStr;
        JSONArray jsonArray = new JSONArray(result);
        Merchant[] merchants = new Merchant[jsonArray.length()-1];
        Log.e("Fawry", "Result => " + result);
        Log.e("Fawry", "length => " + jsonArray.length());

        if (jsonArray.length() == 0) {
            Log.e("Fawry", "in condition ");
            //Toast.makeText(context," لا يوجد فروع " , Toast.LENGTH_SHORT);
        } else {


            Double centerLat = 0.0, centerLon = 0.0;
            try {
                JSONObject object = (JSONObject) jsonArray.get(0);
                centerLat = Double.parseDouble((String) object.get("centerLat"));
                centerLon = Double.parseDouble((String) object.get("centerLon"));

                Log.e("Fawry", "centerLat => " + centerLat);
                Log.e("Fawry", "centerLon => " + centerLon);


            } catch (Exception e) {
                Log.e("Fawry parse", e.toString());
            }

            Merchant m = new Merchant();
            for (int i=1;i <jsonArray.length();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                m.setLatitude(Double.parseDouble(jsonObject.get("lat").toString()));
                m.setLongitude(Double.parseDouble(jsonObject.get("long").toString()));
                m.setName(jsonObject.get("name").toString());
                m.setAddress(jsonObject.get("address").toString());
                m.setPhone(jsonObject.get("phone").toString());
                m.setTerminalID(jsonObject.get("TerminalFawryID").toString());
                m.setMerchantType(jsonObject.get("MerchantTypeName").toString());
               merchants[i-1] = m;

            }
        }
        return merchants;
    }

    @Override
    protected Merchant[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String MoviesJsonStr = null;

        //Press HERE API_KEY
        //  String API_KEY = "674437857f8bd7add004a68e85f81896";
        try {

            final String BASE_URL = "http://sacred-store-100115.appspot.com/API/";
            final String TERMINAL_URL = "findBranchByTerminalNo";
            final String TERMINAL_NO ="terminalno";
            //   final String KEY_PARAM = "api_key";

            Uri BuiltUri = Uri.parse(BASE_URL + TERMINAL_URL).buildUpon().appendQueryParameter(TERMINAL_NO ,params[0])
                    .build();

            URL url = new URL(BuiltUri.toString());
            Log.v("uri=",BuiltUri.toString());
            // Create the request to theMovieDb, and open the connectio
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

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

            MoviesJsonStr = buffer.toString();
            Log.v(LOG_TAG, "JSON String = " + MoviesJsonStr);
        } catch (IOException e) {
            return null;
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
            return getLocationDataFromJson(MoviesJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Merchant[] Merchants) {


        ArrayList<Merchant>  merchants = new ArrayList<Merchant>(Arrays.asList(Merchants));
        Intent MapIntent = new Intent(context, MapsActivity.class);
        if (Merchants == null)
        {
            Toast.makeText(context , "Error in Fetching Data",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else
        {
            Bundle  b = new Bundle();
            b.putParcelableArrayList("merchants",merchants);
             MapIntent.putExtras(b);
            context.startActivity(MapIntent);
            progressDialog.dismiss();
        }



    }

}