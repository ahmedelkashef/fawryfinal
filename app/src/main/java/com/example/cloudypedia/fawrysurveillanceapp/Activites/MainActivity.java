package com.example.cloudypedia.fawrysurveillanceapp.Activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.cloudypedia.fawrysurveillanceapp.Fragments.MainFragment;
import com.example.cloudypedia.fawrysurveillanceapp.R;
import com.example.cloudypedia.fawrysurveillanceapp.Utility;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity {
    protected GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mStatusTextView = (TextView) findViewById(R.id.status);
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, new MainFragment()).commit();
        Utility.setActionBar(getSupportActionBar(),this ,"sign");
    }
  /*  public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_signOut){
            Intent intent = new Intent(this , SignInActivity.class)  ;
            intent.putExtra("IssignedIn" ,false);
            startActivity(intent);//mGoogleApiClient.clearDefaultAccountAndReconnect();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
}
