package com.example.branko.tester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.branko.tester.model.CO2;
import com.example.branko.tester.model.CityInfo;
import com.example.branko.tester.model.TrafficCongestion;
import com.example.branko.tester.services.CitiesIntentService;
import com.example.branko.tester.services.CityDetailsIntentService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    private static final String EXTRA_FIRST_CITY = "com.example.branko.tester.first.city";
    private static final String EXTRA_SECOND_CITY = "com.example.branko.tester.second.city";

    private CityInfo mFirstCity;
    private CityInfo mSecondCity;

    // USED
    private BroadcastReceiver mOnShowCityDetailsNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("NEFLAZA","RAZBERI NE FLAZA");
            mFirstCity = intent.getExtras().getParcelable(EXTRA_FIRST_CITY);
            mSecondCity = intent.getExtras().getParcelable(EXTRA_SECOND_CITY);
            // POPULATE PIECHART
            Toast.makeText(DetailsActivity.this,String.format("%s\n%s",mFirstCity.forToast(), mSecondCity.forToast()),Toast.LENGTH_SHORT).show();
        }
    };

    // USED
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getIntentData();

    }

    // USED
    public static Intent newIntent(Context packageContext,CityInfo firstCity, CityInfo secondCity)
    {
        Intent intent = new Intent(packageContext, DetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(EXTRA_FIRST_CITY,firstCity);
        extras.putParcelable(EXTRA_SECOND_CITY,secondCity);
        intent.putExtras(extras);
        return intent;
    }

    // USED
    public static Intent newBroadcastIntent(CityInfo firstCity,CityInfo secondCity, String action)
    {
        Intent intent = new Intent(action);
        Bundle extras = new Bundle();
        extras.putParcelable(EXTRA_FIRST_CITY,firstCity);
        extras.putParcelable(EXTRA_SECOND_CITY,secondCity);
        intent.putExtras(extras);
        return intent;
    }

    // USED
    private void getIntentData()
    {
        Intent intent = getIntent();
        mFirstCity = intent.getParcelableExtra(EXTRA_FIRST_CITY);
        mSecondCity = intent.getParcelableExtra(EXTRA_SECOND_CITY);
    }

    // USED
    private void startCityDetailsIntentService()
    {
        Intent intent = CityDetailsIntentService.newIntent(DetailsActivity.this,mFirstCity,mSecondCity);
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(CityDetailsIntentService.ACTION_SHOW_NOTIFICATION);
        registerReceiver(mOnShowCityDetailsNotification,filter);
        startCityDetailsIntentService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mOnShowCityDetailsNotification);
    }

    private void populateCharts(CityInfo cityInfo)
    {
        ArrayList<PieEntry> entries = cityInfo.getCongestion().getPieEntries();
        ArrayList<Integer> colors = cityInfo.getCongestion().getColors();

        PieDataSet dataSet = new PieDataSet(entries, "Traffic jam");
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);

        PieChart pieChart = findViewById(R.id.firstCityPieChart);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.invalidate();
    }
}
