package com.example.branko.tester.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Branko on 4/16/2018.
 */

public class CO2 implements Parcelable{
    private String countryCode;
    private float co2;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setCo2(float co2) {
        this.co2 = co2;
    }

    public float getCo2() {
        return co2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(countryCode);
        out.writeFloat(co2);
    }

    public static final Parcelable.Creator<CO2> CREATOR
            = new Parcelable.Creator<CO2>() {
        public CO2 createFromParcel(Parcel in) {
            return new CO2(in);
        }

        public CO2[] newArray(int size) {
            return new CO2[size];
        }
    };

    private CO2(Parcel in) {
        countryCode = in.readString();
        co2 = in.readFloat();
    }
}
