package com.example.cloudypedia.fawrysurveillanceapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;

import android.support.v7.app.ActionBar;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudypedia.fawrysurveillanceapp.Activites.MainActivity;
import com.example.cloudypedia.fawrysurveillanceapp.Activites.SignInActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by dev1 on 5/25/2016.
 */
public class Utility {
    private static String TAG = Utility.class.getSimpleName();
    public static final String PREFS_NAME = "ABCPrefsFile";
    public static final String   PREFS_EMPLOYEE_ID = "employeeId";
    public static final String PREFS_TRACKING_ENABLE = "trackingEnabled";//boolean
    public static final String PREFS_ATTENDANCE_ENABLE = "attendanceEnabled";//boolean
    public static final String PREFS_TRACKING_INTERVAL = "trackingInterval";//long in seconds
    public static final String PREFS_NEED_IMAGE = "needImage";//boolean
    public static final String PREFS_NOTIFICATION_ENABLE = "notificationsEnabled";//boolean
    public static final String PREFS_TRACKING_ALL_TIME = "trackingAllTime";//boolean
    public static final String PREFS_TRACKING_DURING_SHIFT_ONLY = "trackingDuringShiftOnly";//boolean
    public static final String PREFS_LONGITUDE = "longitude";//double
    public static final String PREFS_LATITUDE = "latitude";//double
    public static final String PREFS_AREA_RADUIS = "areaRaduis";//int meters
    public static final String PREFS_WIFI_IDS = "wifiIDs";//string
    public static final String PREFS_SHIFT_START = "startTime";//minutes from starting of day(int)
    public static final String PREFS_SHIFT_END = "endTime";//minutes from starting of day(int)
    public static final String PREFS_SHIFT_TIME_ZONE = "timeZone";//int like +2 -2
    public static final String PREFS_SHIFT_WORK_DAYS = "workDays";//string Sunday is 1 ... Saturday is 7
    public static final String PREFS_ATTENDANCE_IN_WORK_LOCATION_ONLY = "attendanceInWorkLocationOnly";//boolean
    public static final String PREFS_AUTO_LOCATION_ATTENDANCE = "autoLocationAttendance";//boolean
    public static final String PREFS_USE_USER_LOCATION = "useUserLocation";//boolean
    public static final String PREFS_USE_WIFI = "useWifi";//boolean
    public static final String PREFS_USER_ID_TOKEN = "userIdToken";//string
    public static final String PREFS_ATTENDANCE_LOCATION_NOTIFICATION_ENABLED = "attendance.location.notification.enabled";//boolean
    public static final String PREFS_COMPANY_LOGO_URL = "logoUrl";//stirng

    public static final String Name = "nameKey";
    public static final String Id = "IdKey";
    public static final String Email = "emailKey";
    public static final String IdToken = "idTokenKey";

    public static final String PREFS_LAST_WIFI_SSID = "lastSSID";//string
    public static final String PREFS_LAST_LOCATION_LONGITUDE = "last.location.longitude";//double
    public static final String PREFS_LAST_LOCATION_LATITUDE = "last.location.latitude";//double
    public static final String PREFS_LAST_LOCATION_PROVIDERS = "last.location.providers";//string
    public static final String PREFS_LAST_ATTENDANCE_STATUS = "last.attendance.status";//string
    public static final String PREFS_LAST_CONNECTIVITY_CHANGED_TIME = "last.attendance.connectivity.changed.time";//long milisec
    public static final String PREFS_LAST_TRACKING_UPLOAD_TIME = "last.attendance.tracking.upload.time";//long milisec
    public static final String PREFS_LOADED_SETTINGS = "prefs.locaded.settings";


    public static SharedPreferences getPrivateSharedPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Utility.PREFS_NAME, context.MODE_PRIVATE);

        return prefs;
    }

    //remove all preferences
    public static void removeAllPreferences(Context context) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    public static String getPreferredEmail(Context context) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getString(Utility.Email,
                null);
    }

    public static void setPreferredEmail(Context context, String email) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Utility.Email, email);
        editor.commit();
    }

    public static Long getPreferredInterval(Context context) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getLong(Utility.PREFS_TRACKING_INTERVAL,
                0);
    }

    public static void setPreferredInterval(Context context, long interval) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(Utility.PREFS_TRACKING_INTERVAL, interval);
        editor.commit();
    }

    public static boolean getPreferredTrackingEnabled(Context context) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getBoolean(Utility.PREFS_TRACKING_ENABLE,
                false);
    }

    public static void setPreferredTrackingEnabled(Context context, boolean trackingEnabled) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Utility.PREFS_TRACKING_ENABLE, trackingEnabled);
        editor.commit();
    }

    public static boolean getPreferredAttendanceEnabled(Context context) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getBoolean(Utility.PREFS_ATTENDANCE_ENABLE,
                false);
    }

    public static void setPreferredAttendanceEnabled(Context context, boolean attendanceEnabled) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Utility.PREFS_ATTENDANCE_ENABLE, attendanceEnabled);
        editor.commit();
    }

    public static boolean getPreferredNeedImage(Context context) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getBoolean(Utility.PREFS_NEED_IMAGE,
                false);
    }

    public static void setPreferredNeedImage(Context context, boolean needImage) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Utility.PREFS_NEED_IMAGE, needImage);
        editor.commit();
    }

    public static String getPreferredIdToken(Context context) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getString(Utility.IdToken,
                null);
    }

    public static void setPreferredIdToken(Context context, String id) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Utility.IdToken, id);
        editor.commit();
    }
    public static String getPreferredId(Context context) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getString(Utility.PREFS_EMPLOYEE_ID,
                null);
    }

    public static void setPreferredName(Context context, String id) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Name, id);
        editor.commit();
    }
    public static String getPreferredName(Context context) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getString(Utility.Name,
                "");
    }

    public static void setPreferredId(Context context, String id) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Utility.PREFS_EMPLOYEE_ID, id);
        editor.commit();
    }
    public static String getStringPreference(Context context, String key) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getString(key, null);
    }

    public static void setStringPreference(Context context, String key, String value) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static float getFloatPreference(Context context, String key) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getFloat(key, 0);
    }

    public static void setFloatPreference(Context context, String key, float value) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static int getIntPreference(Context context, String key) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getInt(key, 0);
    }

    public static void setIntPreference(Context context, String key, int value) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static boolean getBooleanPreference(Context context, String key) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getBoolean(key, false);
    }

    public static void setBooleanPreference(Context context, String key, boolean value) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static long getLongPreference(Context context, String key) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return prefs.getLong(key, 0);
    }

    public static void setDoublePreference(Context context, String key, double value) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.commit();
    }

    public static double getDoublePreference(Context context, String key) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        return Double.longBitsToDouble(prefs.getLong(key, 0));
    }

    public static void setLongPreference(Context context, String key, long value) {
        SharedPreferences prefs = getPrivateSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }


    public static void initSecuredConnection() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslcontext = SSLContext.getInstance("TLSv1");

        sslcontext.init(null,
                null,
                null);
        SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

        HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);

        HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
    }

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(String msg, Context context) {
        dismissProgressDialog();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public static TimeZone intToTimeZone(int offset) {
        String timeZoneStr = "GMT";

        if (offset >= 0) {
            timeZoneStr += "+" + offset;
        } else {
            timeZoneStr += offset;
        }
        return TimeZone.getTimeZone(timeZoneStr);
    }


/*    public static void sendNotification(Context context, String title, String messageBody) {
        Intent intent = new Intent(context, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 *//**//* Request code *//**//*, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String appName = context.getString(R.string.app_name);
        Resources resources = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setTicker(appName + "...")
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        *//*notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());
    }*/


    public final static String IN = "IN";
    public final static String OUT = "OUT";

    /*public static HashMap<String, Object> saveAttendanceData(Context context, final Bitmap mBitmapToSave, String status, long timeInMillis, double longitude, double latitude) {

        byte[] byteArray = null;
        if (mBitmapToSave != null) {
            ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
            mBitmapToSave.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);
            byteArray = bitmapStream.toByteArray();
        }
        final HashMap<String, Object> locationData = new HashMap<>();


        locationData.put(AttendanceContract.LocationEntry.COLUMN_LATITUDE, latitude);
        locationData.put(AttendanceContract.LocationEntry.COLUMN_LONGITUDE, longitude);
        locationData.put(AttendanceContract.LocationEntry.COLUMN_STATUS, status*//*String.format("%s (%s)", status, place)*//*);
        locationData.put(AttendanceContract.LocationEntry.COLUMN_USER_NAME, Utility.getPreferredDisplayName(context));
        locationData.put(AttendanceContract.LocationEntry.COLUMN_EMAIL, Utility.getPreferredEmail(context));
        //locationData.put("imageID", "canceled");
        locationData.put(AttendanceContract.LocationEntry.COLUMN_IMAGE, byteArray);
        locationData.put(AttendanceContract.LocationEntry.COLUMN_DATE, timeInMillis);

        //edited
        locationData.put(Utility.PREFS_USER_ID_TOKEN, Utility.getStringPreference(context, Utility.PREFS_USER_ID_TOKEN));
        final LocationManager manager = (LocationManager) context.getSystemService(MainActivity.LOCATION_SERVICE);
        locationData.put(AttendanceContract.LocationEntry.COLUMN_PROVIDERS, manager.getProviders(true).toString());

        SyncService.startActionUploadData(context, locationData);

        //start tracking
        if (!((Utility.getPreferredTrackingEnabled(context)
                && Utility.getBooleanPreference(context, Utility.PREFS_TRACKING_ALL_TIME))
                || (Utility.getPreferredTrackingEnabled(context)
                && Utility.getBooleanPreference(context, Utility.PREFS_USE_USER_LOCATION)))) {//if there no global tracking

            if (Utility.getPreferredTrackingEnabled(context)
                    && Utility.getBooleanPreference(context, Utility.PREFS_TRACKING_DURING_SHIFT_ONLY)) {
                if (status.equals(Utility.IN))
                    AlarmService.setTrackingAlarm(context, Utility.getPreferredInterval(context));
                else
                    AlarmService.cancelTrackingAlarm(context);
            }
        }


        //update last attendance status
        if (status.equals(Utility.IN)) {
            Utility.setStringPreference(context, Utility.PREFS_LAST_ATTENDANCE_STATUS, AlarmService.STATUS_IN);
        } else {
            Utility.setStringPreference(context, Utility.PREFS_LAST_ATTENDANCE_STATUS, AlarmService.STATUS_OUT);
        }

        Log.e(TAG, "saveAttendanceData() PREFS_LAST_ATTENDANCE_STATUS: " + Utility.getStringPreference(context, Utility.PREFS_LAST_ATTENDANCE_STATUS));


        return locationData;
    }*/


    public static boolean isWorkDayToDay(Context context) {
        String workDays = Utility.getStringPreference(context, Utility.PREFS_SHIFT_WORK_DAYS);

        if (workDays == null)
            return true;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if (workDays.contains(String.valueOf(calendar.get(Calendar.DAY_OF_WEEK))))
            return true;
        else
            return false;

    }

    public static boolean updateSettings(Context context, String response, ImageView mImage) {

        //init app
     /*   String email = Utility.getPreferredEmail(context);
        Utility.removeAllPreferences(context);
        Utility.setPreferredEmail(context, email);*/
       // AlarmService.cancelApplicationAlarms(context);
        //end init

        if (response == null)
            return false;
/*
        final String KEY_SETTINGS = "settings";
        final String KEY_LOCATION = "workLocation";
        final String KEY_WORK_SHIFT = "workShift";*/
        final String KEY_EMPLOYEE_ID = "employeeId";

        try {
            JSONObject jsonObject = new JSONObject(response);
            //user id token

            String employeeId = jsonObject.getString("employeeId");
            if(employeeId.equals("null"))
            {
                employeeId = "";
            }
            Utility.setPreferredId(context,employeeId);

            Utility.setStringPreference(context, Utility.PREFS_USER_ID_TOKEN,
                    jsonObject.getString(Utility.PREFS_USER_ID_TOKEN));

            return true;

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static String saveImage(Context context, Bitmap image, String fileName) {


        FileOutputStream output = null;

        try {
            String outputName = fileName;

            ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);
            byte[] byteArray = bitmapStream.toByteArray();

            output = context.openFileOutput(outputName, Context.MODE_PRIVATE);

            output.write(byteArray, 0, byteArray.length);

            Log.e(TAG, "saveImage() saved");
            return outputName;

        } catch (Exception e) {
            Log.e(TAG, "saveImage() " + e.getMessage());
        } finally {
            try {
                if (output != null)
                    output.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    public static Bitmap getImage(Context context, String fileName){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bitmap = BitmapFactory.decodeFile(context.getFilesDir().getPath() + "/" + fileName);
        Log.e(TAG, "getImage() bitmap: " + bitmap);
        Log.e(TAG, "getImage() context.getFilesDir(): " + context.getFilesDir().getPath());

//        byte[] byteArray = new byte[0];
//        FileInputStream input = null;
//        try {
//
//            input = context.openFileInput(AppConstants.COMPANY_LOGO_FILE_NAME);
//            int read;
//            byte[] data = new byte[1024];
//            while ((read = input.read(data)) != -1)
//                output.write(data, 0, read);
//
//        } catch (Exception e) {
//            Log.e(TAG, "getCompanyLogo() " + e.getMessage());
//        }finally {
//            if (input != null)
//                try {
//                    input.close();
//                } catch (IOException e) {
//                    Log.e(TAG, "getCompanyLogo() " + e.getMessage());
//                }
//        }
//
//        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }
    public static void uploadReport(Context context,
                                    final Bitmap mBitmapToSave,
                                    long timeInMillis,
                                    double longitude,
                                    double latitude ,
                                    String userName ,
                                    String status,
                                    String comment,
                                    String customFields){

        byte[] byteArray = null;
        if (mBitmapToSave != null) {
            ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
            mBitmapToSave.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);
            byteArray = bitmapStream.toByteArray();
        }
        final HashMap<String, Object> locationData = new HashMap<>();
        String userIdToken = Utility.getStringPreference(context, Utility.PREFS_USER_ID_TOKEN);

        locationData.put("latitude", latitude);
        locationData.put("longitude", longitude);
        locationData.put("status", status /*String.format("%s (%s)", status, place)*/);
        locationData.put("user", userName);
        locationData.put(Utility.PREFS_USER_ID_TOKEN, userIdToken);
        //locationData.put("imageID", "canceled");
        locationData.put("byteArrayImage", byteArray);
        locationData.put("date", timeInMillis);
        locationData.put("comment" , comment);
        locationData.put("customField" , customFields);

        SyncService.startActionUploadData(context, locationData);
    }

    public static void  setActionBar(ActionBar actionBar, final Activity context, String imageButton) {
        // TODO Auto-generated method stub
        LayoutInflater inflator = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar, null);

        if(imageButton == "home") {
            ImageButton homebtn = (ImageButton) v.findViewById(R.id.homeButton);
            homebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }
                    });
                }
            });
        }
        else if (imageButton == "sign")
        {
            ImageButton sign = (ImageButton) v.findViewById(R.id.homeButton);
            sign.setImageResource(R.drawable.signinfinal);
            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(context, SignInActivity.class);
                            intent.putExtra("isreturnedfromhome" ,true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }
                    });



                }
            });
        }
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        actionBar.setCustomView(v);

    }
    public static boolean deleteFile(Context context, String fileName){
        File file = new File(context.getFilesDir() + "/" + fileName);
        return file.delete();
    }

    public static void showMessage(final String message , final Context context){
        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        };
        mainHandler.post(myRunnable);
    }

}
