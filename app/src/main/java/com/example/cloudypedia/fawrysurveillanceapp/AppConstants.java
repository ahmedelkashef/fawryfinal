package com.example.cloudypedia.fawrysurveillanceapp;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

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
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Id = "IdKey";
    public static final String Email = "emailKey";
    public static final String IdToken = "idTokenKey";
    /**
     * get setting url
     */
    public static final String GET_SETTING_URL = BASE_HOST_URL + "/m/getUserSetting";

    /**
     * create upload url
     */
    public static final String CREATE_UPLOAD_URL = BASE_HOST_URL + "/m/upload/createUploadUrl";
    public static final String QUERY_ATTENDANCE_LOG = BASE_HOST_URL + "/m/queryAttendanceLogsOne";
    public static final String GET_CHECKIN_METADATA = BASE_HOST_URL + "/m/getCheckinMetadata";
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


    public static void initSecuredConnection() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslcontext = SSLContext.getInstance("TLSv1");

        sslcontext.init(null,
                null,
                null);
        SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

        HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);

        HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
    }
}
