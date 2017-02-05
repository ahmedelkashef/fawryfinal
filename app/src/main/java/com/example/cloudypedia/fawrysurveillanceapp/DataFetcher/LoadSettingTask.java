package com.example.cloudypedia.fawrysurveillanceapp.DataFetcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.example.cloudypedia.fawrysurveillanceapp.Activites.SignInActivity;
import com.example.cloudypedia.fawrysurveillanceapp.AppConstants;
import com.example.cloudypedia.fawrysurveillanceapp.Utility;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dev1 on 8/31/2016.
 */
public class LoadSettingTask extends AsyncTask<String, Integer, Boolean> {

    protected static final String TAG = LoadSettingTask.class.getSimpleName();

    SignInActivity activity;

    public LoadSettingTask(SignInActivity activity){
        this.activity = activity;
    }

    @Override
    public void onPreExecute() {
        Utility.showProgressDialog("التأكد من المستخدم", activity);

    }

    @Override
    protected Boolean doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        final String BASE_URL = AppConstants.GET_SETTING_URL;

        try {

            Utility.initSecuredConnection();

            // FirebaseMessaging.getInstance().subscribeToTopic("news");
            // Get token
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.e(TAG, "token: " + token);

            Uri.Builder builder = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(AppConstants.PARAM_ID_TOKEN, Utility.getPreferredIdToken(activity));
                    //.appendQueryParameter(AppConstants.PARAM_FIRE_BASE_TOKEN, token);


            Uri builtUri = builder.build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.connect();


            Log.e(TAG, "after connect is");

            //Get response
            if(urlConnection.getInputStream() == null){
                Log.e(TAG, "LoadSettingTask InputStream is: " + null);
                return false;
            }
            InputStream responseStream = new
                    BufferedInputStream(urlConnection.getInputStream());

            BufferedReader responseStreamReader =
                    new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();
            Log.e(TAG, "LoadSettingTask response is: " + response);
            makeToast(response);

            //Close response stream
            responseStream.close();


            return Utility.updateSettings(activity, response, null);


        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            makeToast(e.getMessage());
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

        }

        return false;
    }

    @Override
    public void onPostExecute(Boolean result) {
        Utility.dismissProgressDialog();

        String msg;
        if(result){
           //SharedPreferences sharedpreferences = activity.getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
            msg = "مرحبا " + Utility.getPreferredName(activity);
           //msg = "Hello " + sharedpreferences.getString(AppConstants.Name,"");
            activity.goToMainActivity();
        }else {
            msg = "عفوا المستخدم غير مسجل ..";
            activity.signOut();
        }
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();

    }

    private void makeToast(final String msg){
        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(activity.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        };
        //mainHandler.post(myRunnable);
    }






}
