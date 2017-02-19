package com.example.cloudypedia.fawrysurveillanceapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.cloudypedia.fawrysurveillanceapp.DataFetcher.FetchLocationTask;

/**
 * Created by dev3 on 12/26/2016.
 */

public class Controller {


    Context context;
    ProgressDialog progressDialog;

    public Controller (Context context , ProgressDialog progressDialog)
    {
        this.progressDialog = progressDialog;
        this.context = context;
    }

    public void getBranchesByNearest(String lat, String long_) {

        FetchLocationTask fetchLocationTask = new FetchLocationTask(context, progressDialog);
        fetchLocationTask.execute("nearest" , lat , long_);
    }

    public void findBranchByTerminalNo(String terminalNo , Location location) {

        String latitude  = String.valueOf(location.getLatitude());
        String longtitude = String.valueOf(location.getLongitude());
        FetchLocationTask fetchLocationTask = new FetchLocationTask(context, progressDialog);
        fetchLocationTask.execute("terminalId",terminalNo,latitude,longtitude);
    }
}
