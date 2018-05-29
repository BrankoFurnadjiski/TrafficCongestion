package com.example.branko.tester.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.branko.tester.R;

/**
 * Created by Branko on 5/29/2018.
 */

public class AlertDialogBuilder {

    public static AlertDialog initAlertDialog(Context context, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.alert_dialog_title))
                .setPositiveButton(context.getResources().getString(R.string.alert_dialog_close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                        activity.moveTaskToBack(true);
                        System.exit(0);
                    }
                });
        return builder.create();
    }

    public static void displayAlertDialogFirstActivity(Context context, AlertDialog alertDialog){
        if(!InternetConnectionChecker.haveNetworkConnection(context)){
            alertDialog.show();
        } else {
            if(alertDialog.isShowing()){
                alertDialog.hide();
            }
        }
    }

    public static void displayAlertDialogDetailsActivity(Context context, AlertDialog alertDialog){
        if(!InternetConnectionChecker.haveNetworkConnection(context)){
            alertDialog.show();
        } else {
            if(alertDialog.isShowing()){
                alertDialog.hide();
            }
        }
    }
}
