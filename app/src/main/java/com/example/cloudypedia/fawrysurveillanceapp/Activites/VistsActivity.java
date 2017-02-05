package com.example.cloudypedia.fawrysurveillanceapp.Activites;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudypedia.fawrysurveillanceapp.Classes.Rowcontent;
import com.example.cloudypedia.fawrysurveillanceapp.DataFetcher.FetchVistsTask;
import com.example.cloudypedia.fawrysurveillanceapp.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VistsActivity extends AppCompatActivity {

    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar dateTime = Calendar.getInstance();
    private TextView text ;
    TextView date [], location[];
    private Button btn_date;
    TableLayout tableLayout;
    TableRow tableRow;
    List<Rowcontent> rowcontent;
    Date cuurentdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vists);

        FetchVistsTask fetchVistsTask = new FetchVistsTask();
        fetchVistsTask.execute( Long.toString(dateTime.getTimeInMillis()) , "kashef@cloudypedia.com");
        IntializeViews();
        bulidTable();

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
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
    }
    private void IntializeViews()
    {
        text = (TextView) findViewById(R.id.txt_TextDateTime);
        btn_date = (Button) findViewById(R.id.btn_datePicker);
        tableLayout = (TableLayout) findViewById(R.id.mytable);

    }
    private List <Rowcontent> intializeList ()
    {
        rowcontent = new ArrayList<>();

        rowcontent.add(new Rowcontent("Sunday" , "Cairo"));
        rowcontent.add(new Rowcontent("Monaday" , "Cairo"));
        rowcontent.add(new Rowcontent("Tuesday" , "Cairo"));
        rowcontent.add(new Rowcontent("Thursday" , "Cairo"));
        return rowcontent;
    }
    private void bulidTable()
    {
        rowcontent = intializeList();

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

                        Toast.makeText(getApplicationContext(), "im in Date " + r.getDate() , Toast.LENGTH_SHORT).show();
                    }
                });
        }

    }
    public void setDateAttributes( int i)
    {
        date[i].setPadding(5,0,5,5);
        date[i].setTextSize(19);
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


}