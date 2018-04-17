package com.example.branko.tester.web;

import com.example.branko.tester.model.CO2;
import com.example.branko.tester.model.CityInfo;
import com.example.branko.tester.searchModel.CongestionSearchResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Branko on 4/16/2018.
 */

public interface CitiesApi {

    @GET("/data/rest/traffic/cities")
    Call<List<CityInfo>> getCities(@QueryMap Map<String, String> attributes);

    @GET("/data/country-co2.json")
    Call<List<CO2>> getCO2();

    @GET("/data/rest/traffic/citiesMap")
    Call<CongestionSearchResult> getCongestion(@QueryMap Map<String, String> attributes);
}
