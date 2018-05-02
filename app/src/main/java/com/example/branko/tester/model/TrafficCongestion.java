package com.example.branko.tester.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
/**
 * Created by Branko on 4/16/2018.
 */

public class TrafficCongestion implements Parcelable {

    private double brown;
    private double red;
    private double orange;
    private double green;
    private double grey;
    private double cars;

    public TrafficCongestion(){

    }

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

    public double getCars() { return cars; }

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

    public void setCars(double cars) {  this.cars = cars; }

    public String toString(){
        return String.format("brown: %f, red: %f, orange: %f, green: %f, cars: %f", brown, red, orange, green, cars).toString();
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
        out.writeDouble(cars);
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
        cars = in.readDouble();
    }

    public ArrayList<Double> getPercentValue(){
        double sum = brown + red + orange + green + grey;
        ArrayList<Double> values = new ArrayList<>();
        values.add(brown/sum);
        values.add(red/sum);
        values.add(orange/sum);
        values.add(green/sum);
        values.add(grey/sum);
        return values;
    }
}
