package com.example.branko.tester.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Branko on 4/16/2018.
 */

public class CityInfo implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(country);
        out.writeDouble(lat);
        out.writeDouble(lon);
    }

    public static final Parcelable.Creator<CityInfo> CREATOR
            = new Parcelable.Creator<CityInfo>() {
        public CityInfo createFromParcel(Parcel in) {
            return new CityInfo(in);
        }

        public CityInfo[] newArray(int size) {
            return new CityInfo[size];
        }
    };

    private CityInfo(Parcel in) {
        name = in.readString();
        country = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    public String toString() {
        return String.format("%s, %s", name, country);
    }

}
