package com.example.cloudypedia.fawrysurveillanceapp.Activites;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudypedia.fawrysurveillanceapp.AppConstants;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Report;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Rowcontent;
import com.example.cloudypedia.fawrysurveillanceapp.R;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VistsActivity extends AppCompatActivity {

    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar dateTime = Calendar.getInstance();
    TextView text ;
    TextView date [], location[];
    Button btn_date;
    TableLayout tableLayout;
    TableRow tableRow;
    List<Rowcontent> rowcontent;
    Report [] Reports;
    ImageButton refreshbtn ;



 /*   protected void onStart() {
        super.onStart();
        FetchVistsTask fetchVistsTask = new FetchVistsTask();
        fetchVistsTask.execute( Utility.getStringPreference(this, Utility.PREFS_USER_ID_TOKEN) , Long.toString(dateTime.getTimeInMillis()));
    }
*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    outState.putParcelableArray("ReportKey",Reports);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vists);
    if(savedInstanceState == null)
        {
             Reports = new Report[1];
        }
        else
        {
            Reports = (Report[]) savedInstanceState.getParcelableArray("ReportKey");
        }
        Utility.setActionBar(getSupportActionBar(),this, "home");
        IntializeViews();

        new  FetchVistsTask().execute( Utility.getStringPreference(VistsActivity.this , Utility.PREFS_USER_ID_TOKEN) , Long.toString(dateTime.getTimeInMillis()));

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });
        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              new  FetchVistsTask().execute( Utility.getStringPreference(VistsActivity.this , Utility.PREFS_USER_ID_TOKEN) , Long.toString(dateTime.getTimeInMillis()));
            }
        });
        updateTextLabel();

    }

    private void updateDate(){
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }



    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateTextLabel();
        }
    };



    private void updateTextLabel(){
        text.setText(formatDateTime.format(dateTime.getTime()));
        new  FetchVistsTask().execute( Utility.getStringPreference(VistsActivity.this , Utility.PREFS_USER_ID_TOKEN) , Long.toString(dateTime.getTimeInMillis()));
    }
    private void IntializeViews()
    {
        text = (TextView) findViewById(R.id.txt_TextDateTime);
        btn_date = (Button) findViewById(R.id.btn_datePicker);
        tableLayout = (TableLayout) findViewById(R.id.mytable);
        refreshbtn = (ImageButton) findViewById(R.id.Refresh);

    }
/*    private List <Rowcontent> intializeList ()
    {
        rowcontent = new ArrayList<>();

        rowcontent.add(new Rowcontent("Sunday" , "Cairo"));
        rowcontent.add(new Rowcontent("Monaday" , "Cairo"));
        rowcontent.add(new Rowcontent("Tuesday" , "Cairo"));
        rowcontent.add(new Rowcontent("Thursday" , "Cairo"));
        return rowcontent;
    }*/
    private void bulidTable()
    {
        /*rowcontent = intializeList();*/

        tableLayout.setColumnStretchable(0,true);
        tableLayout.setColumnStretchable(1,true);

            for(int i = 0 ; i<rowcontent.size() ; i++) {
                tableRow = new TableRow(this);
                tableRow.setPadding(5, 5, 5, 5);
                tableRow.setBackgroundColor(getResources().getColor(R.color.table_cell_background));

                date = new TextView[rowcontent.size()];
                date[i] = new TextView(this);

                setDateAttributes(i);

                location = new TextView[rowcontent.size()];
                location[i] = new TextView(this);

                setlocationAttributes(i);

                tableRow.addView(date[i]);
                tableRow.addView(location[i]);
                tableLayout.addView(tableRow);

                date[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Rowcontent r = (Rowcontent) view.getTag();
                     String id = r.getID();
                        for (Report re :Reports) {
                            if(re.getReportId() == id)
                            {
                                Intent intent = new Intent (VistsActivity.this , ViewReportActivity.class);

                                Bundle b = new Bundle ();
                                b.putParcelable("reportKey",re);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        }

                    }
                });
        }

    }
    public void setDateAttributes( int i)
    {
        date[i].setPadding(5,0,5,5);
        date[i].setTextSize(16);
        date[i].setTextColor(getResources().getColor(R.color.date_color_text));
        SpannableString content = new SpannableString(rowcontent.get(i).getDate());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        date[i].setText(content);
        date[i].setTag(rowcontent.get(i));
        date[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        date[i].setClickable(true);

    }
    public void setlocationAttributes( int i)
    {
        location[i].setPadding(5,0,5,5);
        location[i].setTextSize(19);
        location[i].setText(rowcontent.get(i).getLocation());
        location[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
    }


    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    public class FetchVistsTask extends AsyncTask<String,String,Report[]> {

        private final String LOG_TAG = FetchVistsTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {

          Utility.showProgressDialog( "جاري التحميل , انتظر من فضلك..."   , VistsActivity.this);
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
                  //  r.setSalesID(indexedCustomFields.get("salesId").toString());
                    r.setRange(indexedCustomFields.get("range").toString());
                    r.setSalesName(indexedCustomFields.get("salesName").toString());
                    r.setIncidentType(indexedCustomFields.get("incidentType").toString());
                    if(indexedCustomFields.has("location"))
                    r.setLocation(indexedCustomFields.get("location").toString());
                    if(indexedCustomFields.has("terminalSerial"))
                        r.setTerminalSerial(indexedCustomFields.get("terminalSerial").toString());
                    if(indexedCustomFields.has("terminalId"))
                        r.setTerminalId(indexedCustomFields.get("terminalId").toString());
                    if(indexedCustomFields.has("address"))
                        r.setAddress(indexedCustomFields.get("address").toString());
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

            Reports = reports;
            Utility.dismissProgressDialog();
            if (reports != null) {
                rowcontent = new ArrayList<>();
               tableLayout.removeAllViews();
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
    }

}