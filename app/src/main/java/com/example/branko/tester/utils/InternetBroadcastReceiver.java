package com.example.branko.tester.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * Created by Branko on 5/29/2018.
 */

public class InternetBroadcastReceiver {

    public static void registerReceiver(BroadcastReceiver broadcastReceiver, Activity activity){
        IntentFilter internetFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        activity.registerReceiver(broadcastReceiver ,internetFilter);

    }

    public static void unregisterReceiver(BroadcastReceiver broadcastReceiver, Activity activity){
        activity.unregisterReceiver(broadcastReceiver);
    }

}
