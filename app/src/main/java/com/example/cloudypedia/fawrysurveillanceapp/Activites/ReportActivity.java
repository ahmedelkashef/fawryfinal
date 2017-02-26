package com.example.cloudypedia.fawrysurveillanceapp.Activites;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;


public class ReportActivity extends AppCompatActivity {

    public TextView gisLocation, locationtxt, name, merchantId, salesId, salesName, salesEmail, barcodeTxt, date , address,
            terminalId;
    public Button range, BarcodeBtn, save, takephoto;
    public Report report;
    public double Range;
    public Location location;
    public String barcodetxt;
    private ImageView imageView;
    public Bitmap imgbitmap;
    public EditText comment;
    private final int CAMERA_REQUEST = 3;
    public Spinner spinner;
    public JSONObject jsonObject;
    ProgressDialog    progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportlayout);
        if (savedInstanceState == null) {
            barcodetxt = "";

        } else {
            barcodetxt = savedInstanceState.getString("barcodetxt");
        }
        Utility.setActionBar(getSupportActionBar(),this, "home");
     // new FetchMetadataTask().execute(Utility.getStringPreference(ReportActivity.this, Utility.PREFS_USER_ID_TOKEN));
        Intialize_view();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("barcodetxt", barcodetxt);
        super.onSaveInstanceState(outState);
    }

    void Intialize_view() {
        Intialize_data();


        name = (TextView) findViewById(R.id.name_txt);
        name.setText("اسم التاجر : " + report.getName());


        gisLocation = (TextView) findViewById(R.id.GISLocation_txt);
        gisLocation.setText("موقعك الحالي : " + report.getGISLocation());

        locationtxt = (TextView) findViewById(R.id.location_txt);
        locationtxt.setText("موقع التاجر : " + report.getLocation());

        merchantId = (TextView) findViewById(R.id.MerchantID_txt);
        merchantId.setText("الباركود : " + report.getTerminalSerial());

        terminalId = (TextView) findViewById(R.id.TerminalID);
        terminalId.setText("رقم التاجر : " + report.getTerminalId());

        salesId = (TextView) findViewById(R.id.salesId_txt);
        salesId.setText("رقم المندوب : " + report.getSalesID());

        salesName = (TextView) findViewById(R.id.salesName_txt);
        salesName.setText("اسم المندوب : " + report.getSalesName());

        salesEmail = (TextView) findViewById(R.id.salesEmail_txt);
        salesEmail.setText("بريد المندوب : " + report.getSalesEmail());

        barcodeTxt = (TextView) findViewById(R.id.barcode);
        barcodeTxt.setText(" قيمة الباركود : " + barcodetxt);

        address = (TextView) findViewById(R.id.address_txt);
        address.setText("العنوان : " + report.getAddress());

        date = (TextView) findViewById(R.id.date_txt);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm a");//EEEE, MMMM dd, yyyy

        Date date1 = new Date();
        date.setText("التاريخ : " + dateFormat.format(date1));

        range = (Button) findViewById(R.id.toggleButton);
        if (Range > 100.0) {
            range.setBackgroundResource(R.drawable.buttonshapered);

        }
        range.setText("المسافة : " + report.getRange() + " متر");


        imageView = (ImageView) findViewById(R.id.captured_img);
        save = (Button) findViewById(R.id.Save_Incident);
        takephoto = (Button) findViewById(R.id.photo_btn);
        comment = (EditText) findViewById(R.id.comment_txt);
        spinner = (Spinner) findViewById(R.id.type_spinner);

        report.setComment(comment.getText().toString());
        BarcodeBtn = (Button) findViewById(R.id.barcode_btn);
        BarcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScanBarcodeActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 3);
            }

        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imgbitmap == null || barcodetxt.equals("") || Range > 100.0) {

                    Alert_Dialog newDialog = new Alert_Dialog();

                    if (imgbitmap == null) {
                        newDialog.photo = "لا يوجد صورة";
                    }
                    if (!report.getTerminalSerial().equals(barcodetxt)) {
                        if (barcodetxt.isEmpty() || barcodetxt == "لا يوجد بار كود") {
                            newDialog.barcode = "لا يوجد  باركود";
                        } else {
                            newDialog.barcode = "الباركود غير متطابق";
                        }
                    }


                    if (Range > 100) {

                        newDialog.range = "المسافة بعيدة";
                    }
                    checkStatus();
                    report.setComment(comment.getText().toString());
                    jsonObject = setJsonObject();
                    newDialog.reportActivity = ReportActivity.this;
                    newDialog.show(getFragmentManager(), "انذار");
                } else {
                    checkStatus();
                    try {

                        report.setComment(comment.getText().toString());
                        jsonObject = setJsonObject();
                        Utility.uploadReport(getApplicationContext(),
                                imgbitmap,
                                new Date().getTime(),
                                location.getLongitude(),
                                location.getLatitude(),
                                report.getName(),
                                report.getStatus(),
                                report.getComment(),
                                jsonObject.toString());

                         progressDialog = ProgressDialog.show(ReportActivity.this, "" ,"جارى  رفع الزيارة , انتظر من فضلك...", true);

                        Runnable progressRunnable = new Runnable() {

                            @Override
                            public void run() {
                                progressDialog.cancel();
                            }
                        };

                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 20000);
                        ;
                        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                finish();
                            }
                        });

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "There is Error ", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        //onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    barcodetxt = barcode.displayValue;
                    barcodeTxt.setText("قيمة الباركود : " + barcodetxt);
                } else {
                    barcodetxt = "لا يوجد بار كود";
                    barcodeTxt.setText(barcodetxt);
                }
            }

        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {


                try {
                    imgbitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(imgbitmap);

                    save.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }

            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    void Intialize_data() {

        report = new Report();



        Bundle extras = getIntent().getExtras();
        Merchant current_merchant = extras.getParcelable("merchant");

        // sharedpreferences = getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);

        //  report = extras.getParcelable("sales");

        Range = extras.getDouble("distance");
        location =  extras.getParcelable("currentLocation");
                report.setName(current_merchant.getName());
        report.setLocation(Double.toString(current_merchant.getLatitude()) + " , " + Double.toString(current_merchant.getLongitude()));

        if (location != null)
            report.setGISLocation(Double.toString(location.getLatitude()) + " , " + Double.toString(location.getLongitude()));
        else
            Toast.makeText(getApplicationContext(), "خطأ في التواصل .. من فضلك حاول مرة اخري", Toast.LENGTH_SHORT).show();

        report.setTerminalSerial(current_merchant.getTerminalSerial());
        report.setRange(Double.toString(Range));

    /*        report.setSalesID(sharedpreferences.getString(AppConstants.Id,""));
            report.setSalesName(sharedpreferences.getString(AppConstants.Name,""));
            report.setSalesEmail(sharedpreferences.getString(AppConstants.Email,""));*/

        report.setSalesID(Utility.getPreferredId(this));
        report.setSalesName(Utility.getPreferredName(this));
        report.setSalesEmail(Utility.getPreferredEmail(this));
        //report.setTerminalID();
        report.setStatus("غير ناجحة");
        report.setTerminalId(current_merchant.getTerminalID());
        report.setAddress(current_merchant.getAddress());

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public JSONObject setJsonObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("range", report.getRange());
            jsonObject.put("incidentType", spinner.getSelectedItem().toString());
            jsonObject.put("barcode", barcodetxt);
            jsonObject.put("salesName", report.getSalesName());
            jsonObject.put("salesId", report.getSalesID());
            jsonObject.put("location", report.getLocation());
            jsonObject.put("terminalSerial", report.getTerminalSerial());
            jsonObject.put("terminalId", report.getTerminalId());
            jsonObject.put("address", report.getAddress());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }


    public void checkStatus() {
        if (Range <= 100.0 && report.getTerminalSerial() == barcodetxt)
            report.setStatus("ناجحة");
        else if (Range > 100.0 && report.getTerminalSerial() != barcodetxt)
            report.setStatus("غير ناجحة");
        else if (Range > 100.0)
            report.setStatus("غير ناجحة (المسافة تعدت 100 متر)");
        else if (report.getTerminalSerial() != barcodetxt)
            report.setStatus("غير ناجحة (الباركود غير مطابق)");


    }


    // -----------------------------------------------------------------------------------------------------------------------------------------------------------//

 /*   public class FetchMetadataTask extends AsyncTask<String, String, Map<String, CustomFieldEntry>> {

        private final String LOG_TAG = VistsActivity.FetchVistsTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {

            Utility.showProgressDialog("جاري التحميل , انتظر من فضلك...", ReportActivity.this);
        }

        private Map<String, CustomFieldEntry> getLocationDataFromJson(String ReportJsonStr)
                throws JSONException {


            JSONObject Json = new JSONObject(ReportJsonStr);
            JSONObject itemsJson = Json.getJSONObject("items");

            JSONObject customFields = itemsJson.getJSONObject("customFields");
            //   Map<String,Map<String,Object>> customFields  = jsonArray.getJSONObject("customFields");

            Map<String, CustomFieldEntry> customFieldEntryMap = new HashMap<>();
            Map<CustomFieldEntry, String> x;

            Iterator<String> keys = customFields.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject cfe = customFields.getJSONObject(key);
                String displayName = cfe.getString("displayName");
                String predefinedValues = cfe.getString("predefinedValues");
                boolean indexed = cfe.getBoolean("indexed");

                customFieldEntryMap.put(key, new CustomFieldEntry(displayName, predefinedValues, indexed));
            }

            return customFieldEntryMap;
        }

        @Override
        protected Map<String, CustomFieldEntry> doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String ReportJsonStr = null;

            try {
                AppConstants.initSecuredConnection();

                String[] PARMETER_URL = null;

                Uri BuiltUri;

                PARMETER_URL = new String[1];
                PARMETER_URL[0] = "userIdToken";

                //String encodedemail = Uri.encode(strings[1],"UTF-8");
                //   String encodedemail  = URLEncoder.encode(strings[1],"UTF-8");
                BuiltUri = Uri.parse(AppConstants.GET_CHECKIN_METADATA).buildUpon().appendQueryParameter(PARMETER_URL[0], strings[0])
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
        protected void onPostExecute(Map<String, CustomFieldEntry> metadata) {

            Utility.dismissProgressDialog();
            if (metadata != null) {

            } else {
                Toast.makeText(ReportActivity.this, "خطأ في تحميل البيانات", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}