package com.example.cloudypedia.fawrysurveillanceapp;

import android.app.ProgressDialog;
import android.content.Context;
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

    public void getBranchesByNearest(String lat, String long_, String type) {


    }

    public void findBranchByTerminalNo(String terminalNo) {

        FetchLocationTask fetchLocationTask = new FetchLocationTask(context, progressDialog);
        fetchLocationTask.execute(terminalNo);
    }
}
