package com.example.branko.tester.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Branko on 4/16/2018.
 */

public class TrafficCongestion implements Parcelable {

    public TrafficCongestion(){

    }

    private double brown;
    private double red;
    private double orange;
    private double green;
    private double grey;

    public double getBrown() {
        return brown;
    }

    public double getRed() {
        return red;
    }

    public double getOrange() {
        return orange;
    }

    public double getGreen() {
        return green;
    }

    public double getGrey() {
        return grey;
    }

    public void setBrown(double brown) {
        this.brown = brown;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public void setOrange(double orange) {
        this.orange = orange;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public void setGrey(double grey) {
        this.grey = grey;
    }

    public String toString(){
        return String.format("brown: %f, red: %f, orange: %f, green: %f", brown, red, orange, green).toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(brown);
        out.writeDouble(red);
        out.writeDouble(orange);
        out.writeDouble(green);
        out.writeDouble(grey);
    }

    public static final Parcelable.Creator<TrafficCongestion> CREATOR
            = new Parcelable.Creator<TrafficCongestion>() {
        public TrafficCongestion createFromParcel(Parcel in) {
            return new TrafficCongestion(in);
        }

        public TrafficCongestion[] newArray(int size) {
            return new TrafficCongestion[size];
        }
    };

    private TrafficCongestion(Parcel in) {
        brown = in.readDouble();
        red = in.readDouble();
        orange = in.readDouble();
        green = in.readDouble();
        grey = in.readDouble();
    }

    public ArrayList<PieEntry> getPieEntries()
    {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(Float.valueOf(String.format("%f",brown)),"brown"));
        entries.add(new PieEntry(Float.valueOf(String.format("%f",red)),"red"));
        entries.add(new PieEntry(Float.valueOf(String.format("%f",orange)),"orange"));
        entries.add(new PieEntry(Float.valueOf(String.format("%f",green)),"green"));
        entries.add(new PieEntry(Float.valueOf(String.format("%f",grey)),"grey"));
        return entries;
    }

    public ArrayList<Integer> getColors() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("brown"));
        colors.add(Color.RED);
        colors.add(Color.parseColor("orange"));
        colors.add(Color.GREEN);
        colors.add(Color.parseColor("grey"));
        return colors;
    }
}
