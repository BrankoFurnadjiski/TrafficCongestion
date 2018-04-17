package com.example.branko.tester.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.branko.tester.FirstPageActivity;
import com.example.branko.tester.model.CO2;
import com.example.branko.tester.model.CityInfo;
import com.example.branko.tester.model.TrafficCongestion;
import com.example.branko.tester.utils.TrafficFetchr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Branko on 4/16/2018.
 */

public class CitiesIntentService extends IntentService {

    private static String EXTRA_QUERY="Extra_Query";
    public static final String ACTION_SHOW_NOTIFICATION = "com.example.david.lab03.SHOW_NOTIFICATION";

    public CitiesIntentService(){
        super("CitiesIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        TrafficFetchr fetchr = new TrafficFetchr();
        String query = intent.getStringExtra(EXTRA_QUERY);
        try {
            List<CityInfo> cities = fetchr.fetchCities(query);

            Bundle resultData = new Bundle();
            resultData.putParcelableArrayList(FirstPageActivity.CITIES,(ArrayList<CityInfo>)cities);

            Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
            i.putExtras(resultData);
            String city = intent.getStringExtra(FirstPageActivity.AUTOCOMPLETETEXTVIEWNAME);
            i.putExtra(FirstPageActivity.AUTOCOMPLETETEXTVIEWNAME, city);

            sendBroadcast(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Intent newIntent(Context packageContext, String query){
        Intent i = new Intent(packageContext,CitiesIntentService.class);
        i.putExtra(EXTRA_QUERY,query);
        return i;
    }
}