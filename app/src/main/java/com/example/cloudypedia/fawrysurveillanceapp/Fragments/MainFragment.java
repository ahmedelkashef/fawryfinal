package com.example.cloudypedia.fawrysurveillanceapp.Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.cloudypedia.fawrysurveillanceapp.Activites.MapsActivity;
import com.example.cloudypedia.fawrysurveillanceapp.AdressDialog;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.GPSHandller;
import com.example.cloudypedia.fawrysurveillanceapp.Classes.Merchant;
import com.example.cloudypedia.fawrysurveillanceapp.DataFetcher.FetchLocationTask;
import com.example.cloudypedia.fawrysurveillanceapp.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    ImageButton searchByTerminalId;
    ImageButton searchByNearset;
    ProgressDialog progressDialog;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        searchByTerminalId = (ImageButton) rootView.findViewById(R.id.find_by_terminalNo);
         searchByNearset = (ImageButton) rootView.findViewById(R.id.find_by_nearset); ;
        searchByTerminalId.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   OnDisplayClick(view,searchByTerminalId);
                                               }
                                           }
        );
        searchByNearset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnDisplayClick(view , searchByNearset);
            }
        });

        return rootView ;
    }

    public void btnFindByTerminalNo(View V){
        AdressDialog newFragment = new AdressDialog();
        newFragment.setType(AdressDialog.TYPE.TERMINALNO);
        newFragment.show( getFragmentManager(), "البحث برقم التاجر");
    }

    public void OnDisplayClick(View view , ImageButton clickedButton){

        final GPSHandller gpsHandller = new GPSHandller(MainFragment.this.getActivity());
        
                if (gpsHandller.isCanGetLocation()) {
                    if(clickedButton == searchByTerminalId)
                             btnFindByTerminalNo(view);

                    else if (clickedButton == searchByNearset)
                    {
                        Intent intent = new Intent(getActivity() , MapsActivity.class);

                        ArrayList<Merchant> merchants = fillMerchantData();

                        Bundle b = new Bundle();
                        b.putParcelableArrayList("merchants",merchants);
                        intent.putExtras(b);

                        startActivity(intent);
                    }
                }
                else if(!gpsHandller.isGPSEnabled())
                {
                    Initialize_Dialog(R.string.error_gps_provider,R.string.dialog_title_gps);
                }
                else if (!gpsHandller.isNetworkEnabled())
                {
                    Initialize_Dialog(R.string.error_network_provider,R.string.dialog_title_network);
                }
            }

    public ArrayList<Merchant> fillMerchantData()
    {
        ArrayList<Merchant> merchants = new ArrayList<Merchant>();


        merchants.add(new Merchant( 30.0737152,31.3441117,"سيتي ستارز"));
        merchants.add(new Merchant(30.0828434,31.3301495,"قصر البارون"));
        merchants.add(new Merchant( 30.0767541,31.3263964,"فندق سونيستا"));

        return merchants ;


    }
  public void Initialize_Dialog(int message , int title)
  {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setMessage(message)
              .setTitle(title);

      AlertDialog dialog = builder.create();
      dialog.show();

  }
}
