package com.example.branko.tester.utils;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.branko.tester.R;
import com.example.branko.tester.model.CityInfo;


public class CityHolder extends RecyclerView.ViewHolder {

    private TextView cityName;
    private Context context;

    public CityHolder(View itemView) {
        super(itemView);
        cityName = itemView.findViewById(R.id.cityNameTextView);
        this.context = itemView.getContext();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void bindCity(CityInfo city){
        cityName.setText(city.toString());
    }
}
