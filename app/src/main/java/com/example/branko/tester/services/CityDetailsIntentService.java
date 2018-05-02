package com.example.branko.tester.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.branko.tester.DetailsActivity;
import com.example.branko.tester.model.CO2;
import com.example.branko.tester.model.CityInfo;
import com.example.branko.tester.model.TrafficCongestion;
import com.example.branko.tester.utils.TrafficFetchr;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Branko on 4/17/2018.
 */

public class CityDetailsIntentService extends IntentService {

    private static final String EXTRA_FIRST_CITY = "com.example.branko.tester.first.city";
    private static final String EXTRA_SECOND_CITY = "com.example.branko.tester.second.city";
    public static final String ACTION_SHOW_NOTIFICATION = "com.example.branko.tester.SHOW_SERVICE_NOTIFICATION";

    public CityDetailsIntentService()
    {
        super("CityDetailsIntentService");
    }

    // USED
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        TrafficFetchr fetchr = new TrafficFetchr();
        CityInfo firstCity = intent.getExtras().getParcelable(EXTRA_FIRST_CITY);
        CityInfo secondCity = intent.getExtras().getParcelable(EXTRA_SECOND_CITY);
        try {
            double lat1 = firstCity.getLat();
            double lat2 = secondCity.getLat();
            double lng1 = firstCity.getLon();
            double lng2 = secondCity.getLon();
            ArrayList<TrafficCongestion> congestions = (ArrayList<TrafficCongestion>)fetchr.fetchCongestion(lat1,lat2,lng1,lng2);
            ArrayList<CO2> co2s = (ArrayList<CO2>)fetchr.fetchCO2();
            // UPDATE MODELS
            for(CO2 co2 : co2s){
                if(co2.getCountryCode().equals(firstCity.getCountry())){
                    firstCity.setCo2(co2.getCo2());
                }
                if(co2.getCountryCode().equals(secondCity.getCountry())){
                    secondCity.setCo2(co2.getCo2());
                }
            }

            firstCity.setCongestion(congestions.get(0));
            secondCity.setCongestion(congestions.get(1));

            Intent i = DetailsActivity.newBroadcastIntent(firstCity,secondCity,ACTION_SHOW_NOTIFICATION);
            sendBroadcast(i);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // USED
    public static Intent newIntent(Context packageContext,CityInfo firstCity, CityInfo secondCity)
    {
        Intent intent = new Intent(packageContext,CityDetailsIntentService.class);
        Bundle extras = new Bundle();
        extras.putParcelable(EXTRA_FIRST_CITY,firstCity);
        extras.putParcelable(EXTRA_SECOND_CITY,secondCity);
        intent.putExtras(extras);
        return intent;
    }
}
