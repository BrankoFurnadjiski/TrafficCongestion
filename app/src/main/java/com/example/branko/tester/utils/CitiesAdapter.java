package com.example.branko.tester.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import com.example.branko.tester.R;
import com.example.branko.tester.model.CityInfo;


public class CitiesAdapter extends RecyclerView.Adapter<CityHolder>{

    private List<CityInfo> cities;
    private Context context;

    public CitiesAdapter(Context context){
        cities = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_cities_recycler_view_item, parent,false);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityHolder holder, int position) {
        CityInfo city = cities.get(position);
        holder.bindCity(city);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public List<CityInfo> getCities() {
        return cities;
    }

    public void setCities(List<CityInfo> cities) {
        this.cities = cities;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
