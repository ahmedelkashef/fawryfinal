package com.example.cloudypedia.fawrysurveillanceapp;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class SyncService extends IntentService{

    ProgressDialog progressDialog;

    private static final Object lock = new Object();
    // LogCat tag
    private static final String TAG = SyncService.class.getSimpleName();


    private static final String ACTION_UploadData = "com.adnroid.cloudypedia.attendancelocation.service.action.UploadData";
    private static final String ACTION_SYNCDATA = "com.adnroid.cloudypedia.attendancelocation.service.action.ACTION_SYNCDATA";

    private static final String EXTRA_PARAM_LOCATIONDATA = "com.adnroid.cloudypedia.attendancelocation.service.extra.LOCATIONDAT";
    private static final int UPLOAD_LOCATION_NOTIFICATION_ID = 111;


    public SyncService() {
        super("SyncService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        makeToast("onHandleIntent");

        if (intent != null) {
            final String action = intent.getAction();
            if(ACTION_UploadData.equals(action)){
                makeToast("ACTION_UploadData");
                final HashMap<String, Object> locationData = (HashMap<String, Object>) intent.getSerializableExtra(EXTRA_PARAM_LOCATIONDATA);
                handleActionUploadData(locationData);
            }
        }
    }

    public static void startActionUploadData(Context context, HashMap<String, Object> locationData) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_UploadData);
        intent.putExtra(EXTRA_PARAM_LOCATIONDATA, locationData);
        context.startService(intent);
    }

    public static void startActionSyncData(Context context) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_SYNCDATA);
        context.startService(intent);
    }

    private void handleActionUploadData(HashMap<String,Object> locationData) {

        uploadData(locationData);
    }



    private  boolean uploadData(HashMap<String,Object> locationData){
        boolean status = false;
        //make notification
        //makeNotification(this.getString(R.string.not_uploading_data));

        //makeToast(this.getString(R.string.not_uploading_data));
        //showMessage(this.getString(R.string.not_uploading_data));
        HttpURLConnection urlConnection = null;
        String byteArrayImageParam = "byteArrayImage";

        //Static stuff
        String attachmentName = "image";
        String attachmentFileName = "image.png";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        byte[] byteArrayImage = (byte[]) locationData.get(byteArrayImageParam);

        String requestURL = getUploadUrl();
        String charset = "UTF-8";
        MultipartUtility multipart = null;
        try {
            multipart = new MultipartUtility(requestURL, charset);



                /*This is to add parameter values */
            for(String param : locationData.keySet()){
                if(!param.equals(byteArrayImageParam))
                    multipart.addFormField(param, String.valueOf(locationData.get(param)));
            }


                /*This is to add file content*/
            String contentType = "image/png";
            if(byteArrayImage != null)
                multipart.addBytesPart(attachmentName,attachmentFileName,contentType,byteArrayImage);

            List<String> response = multipart.finish();
            //showMessage(this.getString(R.string.not_data_uploaded));

            //make notification
            //showMessage(this.getString(R.string.not_data_uploaded));
            //showAlert(this.getString(R.string.not_data_uploaded));
            //makeNotification(this.getString(R.string.not_data_uploaded));

            status = true;

            Log.e(TAG, "SERVER REPLIED:");
            String responseString = "";
            for (String line : response) {
                Log.e(TAG, "Upload Files Response:::" + line);
// get your server response here.
                responseString += line;
            }
            Log.e(TAG, responseString);
            makeToast(responseString);

        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            Log.e(TAG, "uploadData: " + e.getMessage());
            //make notification
            //showMessage(this.getString(R.string.not_failed_upload_data));
            //showAlert(this.getString(R.string.not_failed_upload_data));
            //makeNotification(this.getString(R.string.not_failed_upload_data));


            e.printStackTrace();
        }finally {
            //hideProgressDialog();
        }

        return status;
    }





    private String getUploadUrl() {
        HttpsURLConnection urlConnection = null;

        try {

            SSLContext sslcontext = SSLContext.getInstance("TLSv1");

            sslcontext.init(null,
                    null,
                    null);
            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

            HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);

            HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String BASE_URL = AppConstants.CREATE_UPLOAD_URL;


            Uri.Builder builder = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter("email", "mohaned@fawry-retail.com");
            Uri builtUri =builder.build();

            URL url = new URL(builtUri.toString());

            // Setup the request:
            urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");

            urlConnection.connect();

            Log.e(TAG, "after connect is");

            //Get response
            if(urlConnection.getInputStream() == null){
                Log.e(TAG, "getUploadUrl InputStream is: " + null);
                return null;
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
            Log.e(TAG, "getUploadUrl response is: " + response);
            makeToast(response);

            //Close response stream
            responseStream.close();

            String uploadUrl = getUrlFromJSON(response);
            return uploadUrl;


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

        return null;
    }

    private String getUrlFromJSON(String response){

        final String RES_ERROR = "error";
        final String RES_MESSAGE = "message";
        final String RES_RESULT = "result";
        final String RES_URL = "uploadUrl";
        try {
            JSONObject resposeJson = new JSONObject(response);
            makeToast(resposeJson.toString());
            Log.e(TAG, "resposeJson.toString() " + resposeJson.toString());

            if(resposeJson.has(RES_ERROR)){
                JSONObject errorJson = resposeJson.getJSONObject(RES_ERROR);
                Log.e(TAG, errorJson.getString(RES_MESSAGE));
            }else{
                makeToast("resposeJson.getString" + resposeJson.getString(RES_URL));
                return resposeJson.getString(RES_URL);
            }
        } catch (JSONException e) {
            Log.e(TAG, "error" + e.getMessage());
        }

        return null;
    }


    private void makeToast(final String msg){
        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(this.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SyncService.this, msg, Toast.LENGTH_SHORT).show();
            }
        };
        //mainHandler.post(myRunnable);
    }

    private void showMessage(final String message){
        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(this.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SyncService.this, message, Toast.LENGTH_LONG).show();
            }
        };
        mainHandler.post(myRunnable);
    }




}
