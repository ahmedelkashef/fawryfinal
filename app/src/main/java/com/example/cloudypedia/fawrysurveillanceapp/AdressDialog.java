package com.example.cloudypedia.fawrysurveillanceapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.cloudypedia.fawrysurveillanceapp.DataFetcher.FetchLocationTask;

/**
 * Created by Mohammad Adnan on 10/26/2015.
 */
public class AdressDialog extends DialogFragment {

    public enum TYPE {ADDRESS, TERMINALNO}

    public void setType(TYPE type) {
        this.type = type;
    }

    private TYPE type = TYPE.ADDRESS;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_adress, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.e("Fawry", "In positiveButton");
                        EditText adressEditText = (EditText)view.findViewById(R.id.et_adress);
                        Log.e("Fawry", "adressEditText: " + adressEditText);
                        String adress = adressEditText.getText().toString();
                        Log.e("Fawry", "adress: " + adress);
                        dialog.dismiss();
                        ProgressDialog progressDialog;
                        progressDialog = ProgressDialog.show(AdressDialog.this.getActivity(), "" ,"جارى التحميل, انتظر من فضلك...", true);
                        Controller controller = new Controller(AdressDialog.this.getActivity(), progressDialog);
                        controller.findBranchByTerminalNo(adress.trim());



                    }
                })
                .setNegativeButton("رجوع", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AdressDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
