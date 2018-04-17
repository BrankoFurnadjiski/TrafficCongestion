package com.example.branko.tester.model;

import java.util.List;

/**
 * Created by Branko on 4/16/2018.
 */

public class CityInfo {

    private String name;
    private String country;
    private double lat;
    private double lon;
    //private TrafficCongestion congestion;

    public CityInfo()
    {

    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() { return lon; }

}
