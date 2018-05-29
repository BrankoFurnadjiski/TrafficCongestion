package com.example.branko.tester.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Branko on 4/16/2018.
 */

public class CityInfo implements Parcelable {

    private String name;
    private String country;
    private double lat;
    private double lon;
    private TrafficCongestion congestion;
    private float co2;

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

    public TrafficCongestion getCongestion(){
        return congestion;
    }

    public void setCongestion(TrafficCongestion congestion){
        this.congestion = new TrafficCongestion();
        this.congestion.setBrown(congestion.getBrown());
        this.congestion.setGreen(congestion.getGreen());
        this.congestion.setGrey(congestion.getGrey());
        this.congestion.setOrange(congestion.getOrange());
        this.congestion.setRed(congestion.getRed());
        this.congestion.setCars(congestion.getCars());
    }

    public float getCo2() {
        return co2;
    }

    public void setCo2(float co2){
        this.co2 = co2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(country);
        out.writeDouble(lat);
        out.writeDouble(lon);
        out.writeParcelable(congestion,flags);
        out.writeFloat(co2);
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
        congestion = in.readParcelable(TrafficCongestion.class.getClassLoader());
        co2 = in.readFloat();
    }

    public String toString() {
        return String.format("%s, %s", name, country);
    }

    public ArrayList<PieEntry> getTrafficPieEntries(){
        ArrayList<Double> values = congestion.getPercentValue();
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(Float.valueOf(String.format("%.2f",values.get(0)*100)),"brown"));
        entries.add(new PieEntry(Float.valueOf(String.format("%.2f",values.get(1)*100)),"red"));
        entries.add(new PieEntry(Float.valueOf(String.format("%.2f",values.get(2)*100)),"orange"));
        entries.add(new PieEntry(Float.valueOf(String.format("%.2f",values.get(3)*100)),"green"));
        entries.add(new PieEntry(Float.valueOf(String.format("%.2f",values.get(4)*100)),"grey"));
        return entries;
    }

    public ArrayList<Integer> getTrafficColors() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#A52A2A"));
        colors.add(Color.parseColor("#FF0000"));
        colors.add(Color.parseColor("#FF9F00"));
        colors.add(Color.parseColor("#00B420"));
        colors.add(Color.parseColor("#808080"));
        return colors;
    }

    public ArrayList<PieEntry> getCO2PieEntries() {
        if(co2 == 0)
            setCo2(133);
        double pieValue = congestion.getCars() * co2 / 1000;
        double percent = 100 * (pieValue / congestion.getCars());

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(Float.valueOf(String.format("%.2f",percent)),"green"));
        entries.add(new PieEntry(Float.valueOf(String.format("%.2f",100-percent)),"grey"));
        return entries;
    }

    public ArrayList<Integer> getCO2Colors() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#99CC00"));
        colors.add(Color.parseColor("#EAEAEA"));
        return colors;
    }

    public ArrayList<BarEntry> getBarEntries() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<Integer> values = getBarEntryValues();

        for (int i = 0; i < values.size(); ++i) {
            barEntries.add(new BarEntry(i+1, values.get(i)));
        }

        return barEntries;
    }

    private ArrayList<Integer> getBarEntryValues(){
        double factor = 156543.03392 * Math.cos(lat * Math.PI / 180) / Math.pow(2, 16);
        ArrayList<Integer> values = new ArrayList<>();

        values.add((int)Math.floor((factor * congestion.getOrange())/200));
        values.add((int)Math.floor((factor * congestion.getRed())/150));
        values.add((int)Math.floor((factor * congestion.getBrown())/100));

        return values;
    }

    public double getCO2PieChartCenterValue(){
        return congestion.getCars() * co2 / 1000;
    }

}
