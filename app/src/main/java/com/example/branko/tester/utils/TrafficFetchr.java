package com.example.branko.tester.utils;

import com.example.branko.tester.model.CO2;
import com.example.branko.tester.model.CityInfo;
import com.example.branko.tester.model.TrafficCongestion;
import com.example.branko.tester.searchModel.CongestionSearchResult;
import com.example.branko.tester.web.CitiesApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Branko on 4/16/2018.
 */

public class TrafficFetchr {

    private Map<String,String> attributes;

    public TrafficFetchr(){
        attributes = new HashMap<>();
    }

    private CitiesApi establishConnection(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://traffic.b1.finki.ukim.mk/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(CitiesApi.class);
    }

    public List<CityInfo> fetchCities(String query) throws IOException{
        attributes.put("name",query);
        CitiesApi service = establishConnection();
        Call<List<CityInfo>> call = service.getCities(attributes);
        List<CityInfo> result = call.execute().body();
        if(result != null){
            return result;
        }
        return null;
    }

    public List<TrafficCongestion> fetchCongestion(double lat1, double lat2, double lng1, double lng2) throws IOException{
        attributes.put("lat1",Double.toString(lat1));
        attributes.put("lat2",Double.toString(lat2));
        attributes.put("lng1",Double.toString(lng1));
        attributes.put("lng2",Double.toString(lng2));
        attributes.put("zoom","16");
        CitiesApi service = establishConnection();
        Call<CongestionSearchResult> call = service.getCongestion(attributes);
        CongestionSearchResult result = call.execute().body();
        if(result != null){
            return result.getSearchResult();
        }
        return null;
    }

    public List<CO2> fetchCO2() throws IOException{
        CitiesApi service = establishConnection();
        Call<List<CO2>> call = service.getCO2();
        List<CO2> result = call.execute().body();
        if(result != null){
            return result;
        }
        return null;
    }
}
