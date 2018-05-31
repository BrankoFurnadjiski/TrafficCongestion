package com.example.branko.tester.utils;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.branko.tester.CitiesActivity;
import com.example.branko.tester.DetailsActivity;
import com.example.branko.tester.R;
import com.example.branko.tester.model.CityInfo;


public class CityHolder extends RecyclerView.ViewHolder {

    private CityInfo city;
    private TextView cityName;
    private Context context;

    public CityHolder(View itemView) {
        super(itemView);
        cityName = itemView.findViewById(R.id.cityNameTextView);
        this.context = itemView.getContext();

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CitiesActivity activity = (CitiesActivity) context;
                CityInfo firstCity = activity.getmFirstCity();
                CityInfo secondCity = activity.getmSecodnCity();
                LinearLayout layout = view.findViewById(R.id.recycler_view_item);
                layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                if(firstCity == null){
                    firstCity = city;
                } else {
                    secondCity = city;
                }
                Intent intent = DetailsActivity.newIntent(context, firstCity, secondCity);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CitiesActivity activity = (CitiesActivity) context;
                activity.hideKeyboard();
                return false;
            }
        });
    }

    public void bindCity(CityInfo city){
        this.city = city;
        cityName.setText(city.toString());
    }
}
