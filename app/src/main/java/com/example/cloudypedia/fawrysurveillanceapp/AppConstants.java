package com.example.cloudypedia.fawrysurveillanceapp;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * Application constants.
 */
public class AppConstants {
    /**
     * Your WEB CLIENT ID from the API Access screen of the Developer Console for your project. This
     * is NOT the Android client id from that screen.
     *
     * @see <a href="https://developers.google.com/console">https://developers.google.com/console</a>
     */
    public static final String SERVER_CLIENT_ID = "584666349039-hapvscpag4083e3vnai97a1on2m60cjm.apps.googleusercontent.com";

    /**
     * app url
     */
    public static final String BASE_HOST_URL = "https://4-dot-cloudypedia-abc.appspot.com/a";//"https://cloudypedia-abc.appspot.com/a";//

    /**
     * get setting url
     */
    public static final String GET_SETTING_URL = BASE_HOST_URL + "/m/getUserSetting";

    /**
     * create upload url
     */
    public static final String CREATE_UPLOAD_URL = BASE_HOST_URL + "/m/upload/createUploadUrl";

    /**
     * upload tracking data url
     */
    public static final String UPLOAD_TRACKING_URL = BASE_HOST_URL + "/m/upload/tracking";

    /**
     * upload attendance data url
     */
    public static final String UPLOAD_ATTENDANCE_URL = BASE_HOST_URL + "/m/upload/attendance";

    public static final String PARAM_ID_TOKEN = "idToken";
    public static final String PARAM_FIRE_BASE_TOKEN = "fireBaseToken";

    /**
     * The audience is defined by the web client id, not the Android client id.
     */
    public static final String AUDIENCE = "server:client_id:" + SERVER_CLIENT_ID;

    public static final String COMPANY_LOGO_FILE_NAME = "companyLogo.png";
    public static final int LOCATION_ACCURACY = 100; //meters

    public static void  test(Context context, final Bitmap mBitmapToSave, long timeInMillis, double longitude, double latitude , String customfield){
        byte[] byteArray = null;
        if (mBitmapToSave != null) {
            ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
            mBitmapToSave.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);
            byteArray = bitmapStream.toByteArray();
        }
        final HashMap<String, Object> locationData = new HashMap<>();


        locationData.put("latitude", latitude);
        locationData.put("longitude", longitude);
        locationData.put("status", "IN"/*String.format("%s (%s)", status, place)*/);
        locationData.put("user", "Kashef");
        locationData.put("email", "mohaned@fawry-retail.com");
        //locationData.put("imageID", "canceled");
        locationData.put("byteArrayImage", byteArray);
        locationData.put("date", timeInMillis);
        locationData.put("custom field" , customfield);

        SyncService.startActionUploadData(context, locationData);
    }
}
