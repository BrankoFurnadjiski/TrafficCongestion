package com.example.branko.tester.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.branko.tester.FirstPageActivity;
import com.example.branko.tester.model.CityInfo;
import com.example.branko.tester.utils.TrafficFetchr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Branko on 4/16/2018.
 */

public class CitiesIntentService extends IntentService {

    private static final String EXTRA_QUERY="com.example.branko.tester.services.query";
    private static final String ARRAY_LIST_QUERY_RESULT = "com.example.branko.tester.services.array.list.query.result";
    public static final String CITY_NAME_QUERY = "com.example.branko.tester.services.city.name.query";

    public static final String ACTION_SHOW_NOTIFICATION = "com.example.david.lab03.SHOW_NOTIFICATION";


    public CitiesIntentService(){
        super("CitiesIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        TrafficFetchr fetchr = new TrafficFetchr();
        String query = intent.getStringExtra(EXTRA_QUERY);
        String arrayListQuery = intent.getStringExtra(ARRAY_LIST_QUERY_RESULT);
        String cityNameQuery = intent.getStringExtra(CITY_NAME_QUERY);
        try {
            List<CityInfo> cities = fetchr.fetchCities(query);

            Bundle resultData = new Bundle();
            resultData.putParcelableArrayList(arrayListQuery,(ArrayList<CityInfo>)cities);
            Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
            i.putExtras(resultData);
            if(cityNameQuery != null) {
                String city = intent.getStringExtra(cityNameQuery);
                i.putExtra(cityNameQuery, city);
            }

            sendBroadcast(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Intent newIntent(Context packageContext, String query, String listNotifier, String cityNotifier){
        Intent i = new Intent(packageContext,CitiesIntentService.class);
        i.putExtra(EXTRA_QUERY,query);
        i.putExtra(ARRAY_LIST_QUERY_RESULT,listNotifier);
        i.putExtra(CITY_NAME_QUERY,cityNotifier);
        return i;
    }
}