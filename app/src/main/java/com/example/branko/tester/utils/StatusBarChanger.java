package com.example.branko.tester.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;

/**
 * Created by Branko on 5/29/2018.
 */

public class StatusBarChanger {
    public static void changeColorForStatusBar(Activity activity){
        if (Build.VERSION.SDK_INT > 21) {
            activity.getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }
    }

}
