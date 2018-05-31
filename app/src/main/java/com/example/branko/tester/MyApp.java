package com.example.branko.tester;

import android.app.Application;
import android.util.Log;

import com.example.branko.tester.utils.TypefaceUtil;

/**
 * Created by Branko on 5/31/2018.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Adequate-ExtraLight.ttf");
    }
}
